package pl.edu.ug.kglab.ReactiveTestApp.user.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String name;

    private String lastname;

    private String email;

    private Date registeredAt;

    private List<Address> addresses;

    private Profile profile;

    private List<String> phoneNumbers;
}

