package burunzhuy.resource.user;

import burunzhuy.entity.User;
import burunzhuy.enums.user.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
public class UserResource {
    private final Long id;
    private final String email;
    private final String name;
    private final String secondName;
    private final String lastName;
    private final Long phone;
    private final Set<Role> roles;

    public UserResource(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.secondName = user.getSecondName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.roles = user.getRole();
    }
}
