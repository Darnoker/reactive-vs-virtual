package pl.edu.ug.kglab.ReactiveTestApp.benchmark;


import io.netty.channel.ChannelOption;
import org.openjdk.jmh.annotations.*;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import pl.edu.ug.kglab.ReactiveTestApp.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class OrderControllerBenchmark {

    private WebClient webClient;

    private List<String> orderIds;

    @Setup(Level.Trial)
    public void setUp() throws Exception {
        initializeOrderIds();

        initializeWebClient();

        checkServerConnection();
    }

    private void initializeWebClient() {
        ConnectionProvider provider = ConnectionProvider.builder("custom")
                .maxConnections(orderIds.size())
                .pendingAcquireMaxCount(orderIds.size())
                .build();

        HttpClient httpClient = HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .responseTimeout(Duration.ofSeconds(15));

        webClient = WebClient.builder()
                .baseUrl("http://127.0.0.1:8080")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
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
            throw new IllegalStateException("Server not available on localhost:8080");
        }
    }

    @Benchmark
    public List<List<Product>> benchmarkConcurrentRequests() {
        List<Mono<List<Product>>> requestMonos = orderIds.stream()
                .map(id -> webClient.get()
                        .uri("/orders/products/" + id)
                        .retrieve()
                        .bodyToFlux(Product.class)
                        .collectList())
                .collect(Collectors.toList());

        return Flux.merge(requestMonos)
                .collectList()
                .block();
    }

    private void initializeOrderIds() throws Exception {
        Path path = Path.of("ReactiveTestApp/data/orderIds.txt");

        if (!Files.exists(path)) {
            System.err.println("Brak pliku w lokalizacji: " + path);
            throw new Exception();
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            orderIds = reader.lines().toList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
