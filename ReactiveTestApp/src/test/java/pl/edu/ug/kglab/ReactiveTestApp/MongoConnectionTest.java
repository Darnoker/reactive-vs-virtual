package pl.edu.ug.kglab.ReactiveTestApp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.test.StepVerifier;

import java.util.Objects;

@SpringBootTest
public class MongoConnectionTest {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Test
    public void testConnection() {
        // Check if the "users" collection exists (this will trigger a connection to MongoDB)
        StepVerifier.create(reactiveMongoTemplate.collectionExists("users"))
                .expectNextMatches(Objects::nonNull)
                .verifyComplete();
    }
}
