package burunzhuy.tool.notificators;

import burunzhuy.interfaces.notification.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("emailPasswordRestoreSender")
@RequiredArgsConstructor
public class EmailPasswordRestoreSender
        extends BasePasswordRestoreSender {

    @Qualifier("resendEmailSender")
    private final EmailSender emailSender;

    @Override
    public boolean send() {
        return emailSender.send(this.user.getEmail(), this.getSubject(), this.getHtmlMessage(), Optional.of(""));
    }
}
