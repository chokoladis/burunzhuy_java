package burunzhuy.tool.notificators;

import burunzhuy.interfaces.notification.EmailSender;
import burunzhuy.tool.Logger;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("resendEmailSender")
public class ResendEmailSender implements EmailSender {

    @Value("${resend.api-key}")
    private String apiKey;
    @Value("${resend.email-from}")
    private String emailFrom;

    @Override
    public boolean send(String to, String subject, String html, Optional<String> from) {
        Resend resend = new Resend(this.apiKey);

        StringBuilder emailFrom = new StringBuilder(this.emailFrom);

        if (!from.isEmpty()) {
            emailFrom.append(from.get());
        }

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(String.format("Acme <%s>", emailFrom))
                .to(to)
                .subject(subject)
                .html(html)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            return true;
        } catch (ResendException e) {
            e.printStackTrace();
            Logger.logToFile("emailSender.txt", e.getMessage());
            return false;
        }
    }
}
