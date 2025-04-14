package pl.edu.ug.kglab.VirtualThreadsTestApp.health;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class HealthController {

    @GetMapping("/thread-health")
    @SneakyThrows
    public CompletableFuture<String> getThreadName() {
        return CompletableFuture.supplyAsync(() -> Thread.currentThread().toString());
    }
}
