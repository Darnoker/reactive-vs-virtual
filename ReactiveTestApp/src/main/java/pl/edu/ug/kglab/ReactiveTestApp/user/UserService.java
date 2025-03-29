package pl.edu.ug.kglab.ReactiveTestApp.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import pl.edu.ug.kglab.ReactiveTestApp.user.model.User;
import reactor.core.publisher.Flux;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    private final ReactiveMongoTemplate mongoTemplate;

    public UserService(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
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

