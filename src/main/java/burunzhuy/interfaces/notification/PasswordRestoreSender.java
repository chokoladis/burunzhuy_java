package burunzhuy.interfaces.notification;

import burunzhuy.entity.user.User;

public interface PasswordRestoreSender {
    void setUser(User user);
    void setToken(String token);
    boolean send();
}
