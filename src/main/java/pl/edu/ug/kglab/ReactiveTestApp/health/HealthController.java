package pl.edu.ug.kglab.ReactiveTestApp.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/myHealth")
    public String get() {
        return appName;
    }
}
