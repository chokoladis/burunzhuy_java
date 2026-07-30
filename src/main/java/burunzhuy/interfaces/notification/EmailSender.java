package burunzhuy.interfaces.notification;

import java.util.Optional;

public interface EmailSender {
    boolean send(String to, String subject, String html, Optional<String> from);
}
