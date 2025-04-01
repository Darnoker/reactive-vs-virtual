package pl.edu.ug.kglab.VirtualThreadsTestApp.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.ug.kglab.VirtualThreadsTestApp.user.model.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class UserController {

    private final UserService userService;

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public CompletableFuture<List<User>> findUsersByLastname(@PathVariable String lastname) {
        return CompletableFuture.supplyAsync(() -> userService.findUsersByLastname(lastname));
    }


}
