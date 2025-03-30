package pl.edu.ug.kglab.ReactiveTestApp.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import pl.edu.ug.kglab.ReactiveTestApp.user.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final ReactiveMongoTemplate mongoTemplate;

    public UserService(UserRepository userRepository, ReactiveMongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public Mono<User> getUser(String userId) {
        return userRepository.findById(userId);
    }

    public Mono<Boolean> doesUserExists(String id) {
        return userRepository.existsById(id);
    }


    public Flux<User> getAllUsers() {
        Query query = new Query();
        return mongoTemplate.find(query, User.class)
                .doOnSubscribe(subscription -> logger.info("Zapytanie do MongoDB rozpoczęte..."))
                .doOnCancel(() -> logger.info("Zapytanie anulowane."))
                .doOnComplete(() -> logger.info("Zakończono pobieranie użytkowników"))
                .doOnError(error -> logger.error("Błąd podczas pobierania użytkowników", error));
    }
}

