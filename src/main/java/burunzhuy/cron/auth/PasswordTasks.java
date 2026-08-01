package burunzhuy.cron.auth;

import org.springframework.scheduling.annotation.Scheduled;

public class PasswordTasks {
    @Scheduled(cron = "0 0 2 * * *")
    public void cleanOld()
    {

    }
}
