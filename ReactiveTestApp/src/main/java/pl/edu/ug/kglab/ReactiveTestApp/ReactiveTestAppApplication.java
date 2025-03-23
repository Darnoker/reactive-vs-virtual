package pl.edu.ug.kglab.ReactiveTestApp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@SpringBootApplication
@Slf4j
public class ReactiveTestAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveTestAppApplication.class, args);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.create("http://localhost:8080");
    }

    @Value("${benchmark.file.path}")
    private String benchmarkOrderIdsFilePath;

    @Value("${benchmark.number.orderIds}")
    private Integer numberOfOrderIds;


    @Bean
    public CommandLineRunner callEndpointOnStartup(WebClient webClient) {
        return args -> {
            File file = new File(benchmarkOrderIdsFilePath);
            if (file.exists()) {
                return;
            }
            Path path = Path.of(benchmarkOrderIdsFilePath);
            webClient.get()
                    .uri("/orders/ids/" + numberOfOrderIds)
                    .retrieve()
                    .bodyToFlux(String.class)
                    .collectList()
                    .doOnSuccess(orderIds -> log.info("Pobrano {} orderId do pliku {}", orderIds.size(), benchmarkOrderIdsFilePath))
                    .doOnError(error -> log.error("Błąd przy wczytywaniu orderIds"))
                    .subscribe(responseList -> writeOrderIdsToFile(responseList, path));
        };
    }

    private void writeOrderIdsToFile(List<String> orderIds, Path path) {
        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                for (String orderId : orderIds) {
                    writer.write(orderId);
                    writer.newLine();
                }
                writer.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException("Błąd zapisu do pliku: " + path, e);
        }
    }
}
