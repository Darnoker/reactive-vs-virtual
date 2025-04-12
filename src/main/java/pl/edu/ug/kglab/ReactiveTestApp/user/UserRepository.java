package pl.edu.ug.kglab.ReactiveTestApp.user;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pl.edu.ug.kglab.ReactiveTestApp.user.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Flux<User> findByLastname(String lastname);

    Mono<Boolean> existsById (String id);
}
