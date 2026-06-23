package burunzhuy.resource.user;

import burunzhuy.entity.User;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@JsonPropertyOrder({"id", "email", "name", "secondName", "lastName", "phone", "roles"})
public class UserResource {
    private final Long id; // need id ?
    private final String email;
    private final String name;
    private final String secondName;
    private final String lastName;
    private final Long phone;
    private final Set<String> roles;

    public UserResource(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.secondName = user.getSecondName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.roles = user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }
}
