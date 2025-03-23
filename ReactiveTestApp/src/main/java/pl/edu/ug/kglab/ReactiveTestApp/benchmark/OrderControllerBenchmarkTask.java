package pl.edu.ug.kglab.ReactiveTestApp.benchmark;

import io.netty.channel.ChannelOption;
import lombok.SneakyThrows;
import net.openhft.chronicle.jlbh.JLBH;
import net.openhft.chronicle.jlbh.JLBHOptions;
import net.openhft.chronicle.jlbh.JLBHTask;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import pl.edu.ug.kglab.ReactiveTestApp.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class OrderControllerBenchmarkTask implements JLBHTask {

    private WebClient webClient;
    private List<String> orderIds;
    private JLBH jlbh;

    public static void main(String[] args) {
        JLBHOptions options = new JLBHOptions()
                .warmUpIterations(5)
                .iterations(10)
                .throughput(500)
                .accountForCoordinatedOmission(false)

                .jlbhTask(new OrderControllerBenchmarkTask())
                .runs(1);

        // Uruchomienie benchmarku
        new JLBH(options).start();
    }

    @Override
    @SneakyThrows
    public void init(JLBH jlbh) {
        this.jlbh = jlbh;
        initializeOrderIds();
        initializeWebClient();
        checkServerConnection();
    }

    @Override
    public void run(long startTimeNS) {
        Flux.fromIterable(orderIds)
                .parallel(10)
                .runOn(Schedulers.boundedElastic())
                .flatMap(id -> webClient.get()
                        .uri("/orders/products/" + id)
                        .retrieve()
                        .bodyToFlux(Product.class)
                        .collectList())
                .sequential()
                .doOnNext(list -> jlbh.sample(System.nanoTime() - startTimeNS))
                .doOnComplete(() -> System.out.println("Strumień zakończony"))
                .blockLast(Duration.ofSeconds(30));
    }

    private void checkServerConnection() throws InterruptedException {
        boolean serverAvailable = false;
        int retries = 5;
        while (!serverAvailable && retries-- > 0) {
            try {
                webClient.get()
                        .uri("/actuator/health")
                        .retrieve()
                        .bodyToMono(String.class)
                        .block(Duration.ofSeconds(2));
                serverAvailable = true;
            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
        if (!serverAvailable) {
            throw new IllegalStateException("Serwer nie jest dostępny na localhost:8080");
        }
    }

    private void initializeOrderIds() throws Exception {
        Path path = Path.of("ReactiveTestApp/data/orderIds.txt");

        if (!Files.exists(path)) {
            System.err.println("Brak pliku w lokalizacji: " + path);
            throw new Exception("Nie znaleziono pliku z identyfikatorami zamówień.");
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            orderIds = reader.lines().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeWebClient() {
        ConnectionProvider provider = ConnectionProvider.builder("custom")
                .maxConnections(orderIds.size())
                .pendingAcquireTimeout(Duration.ofMillis(5000))
                .maxConnectionPools(orderIds.size())
                .build();

        HttpClient httpClient = HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .responseTimeout(Duration.ofSeconds(30));

        webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
