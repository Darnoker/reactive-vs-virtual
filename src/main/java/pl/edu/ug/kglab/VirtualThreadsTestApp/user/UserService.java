package pl.edu.ug.kglab.VirtualThreadsTestApp.user;

import ch.qos.logback.core.pattern.parser.OptionTokenizer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.edu.ug.kglab.VirtualThreadsTestApp.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUser(String userId) {
        return userRepository.findById(userId);
    }

    public List<User> findUsersByLastname(String lastname) {
        return userRepository.findByLastname(lastname);
    }

    public boolean doesUserExist(String userId) {
        return userRepository.existsById(userId);
    }

    public List<String> listFirstLastnames(int limit) {
        return userRepository.findAll(PageRequest.of(0, limit)).stream()
                .map(User::getLastname)
                .toList();
    }

    public List<String> listFirstIds(int limit) {
        return userRepository.findAll(PageRequest.of(0, limit)).stream()
                .map(User::getId)
                .toList();
    }
}
