package pl.edu.ug.kglab.VirtualThreadsTestApp.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.ug.kglab.VirtualThreadsTestApp.user.model.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/lastname/{lastname}")
    public CompletableFuture<List<User>> findUsersByLastname(@PathVariable String lastname) {
        return CompletableFuture.supplyAsync(() -> userService.findUsersByLastname(lastname));
    }

    @GetMapping("lastname/list/{limit}")
    public CompletableFuture<List<String>> getFirstUserLastnames(@PathVariable Integer limit) {
        return CompletableFuture.supplyAsync(() -> userService.listFirstLastnames(limit));
    }

    @GetMapping("/list/{limit}")
    public CompletableFuture<List<String>> getFirstUsersIds(@PathVariable Integer limit) {
        return CompletableFuture.supplyAsync(() -> userService.listFirstIds(limit));
    }
}
