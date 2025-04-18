package pl.edu.ug.kglab.VirtualThreadsTestApp.health;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class HealthController {

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/myHealth")
    @SneakyThrows
    public CompletableFuture<String> getThreadName() {
        return CompletableFuture.supplyAsync(() -> Thread.currentThread() + " " + appName);
    }
}
