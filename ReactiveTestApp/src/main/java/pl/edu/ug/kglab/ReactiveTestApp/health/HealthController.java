package pl.edu.ug.kglab.ReactiveTestApp.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/myHealth")
    public String get() {
        return "OK";
    }
}
