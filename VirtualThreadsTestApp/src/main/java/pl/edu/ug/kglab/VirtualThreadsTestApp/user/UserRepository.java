package pl.edu.ug.kglab.VirtualThreadsTestApp.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.edu.ug.kglab.VirtualThreadsTestApp.user.model.User;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    List<User> findByLastname(String lastname);

    boolean existsById(String id);
}
