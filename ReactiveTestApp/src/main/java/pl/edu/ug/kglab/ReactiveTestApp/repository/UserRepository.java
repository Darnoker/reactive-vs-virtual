package pl.edu.ug.kglab.ReactiveTestApp.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pl.edu.ug.kglab.ReactiveTestApp.model.user.User;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Flux<User> findByLastname(String lastname);
}
