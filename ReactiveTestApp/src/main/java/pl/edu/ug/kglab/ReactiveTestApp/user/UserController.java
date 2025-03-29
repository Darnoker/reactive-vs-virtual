package pl.edu.ug.kglab.ReactiveTestApp.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.ug.kglab.ReactiveTestApp.user.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping
    public Flux<User> getAllUsers() {
        AtomicLong counter = new AtomicLong();
        return userService.getAllUsers()
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(user -> counter.incrementAndGet())
                .doOnComplete(() -> System.out.println("Liczba użytkowników: " + counter.get()));
    }

    @GetMapping("/lastname/{lastname}")
    public Flux<User> getUsersByLastname(@PathVariable String lastname) {
        return userRepository.findByLastname(lastname);
    }

    @GetMapping("/{id}")
    public Mono<User> getUserById(@PathVariable String id) {
        return userRepository.findById(id);
    }
}
