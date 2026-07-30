package burunzhuy.service.auth;

import burunzhuy.dto.auth.PasswordResetConfirmRequest;
import burunzhuy.dto.auth.PasswordResetRequest;
import burunzhuy.entity.auth.PasswordRestore;
import burunzhuy.entity.user.User;
import burunzhuy.exception.RateLimitException;
import burunzhuy.exception.auth.UserException;
import burunzhuy.exception.common.EntityNotFound;
import burunzhuy.exception.common.NotificationSendException;
import burunzhuy.helper.SecureHelper;
import burunzhuy.interfaces.notification.PasswordRestoreSender;
import burunzhuy.repository.PasswordRestoreRepository;
import burunzhuy.repository.UserRepository;
import burunzhuy.tool.secure.ArgonEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.OptionalInt;

@Service
@RequiredArgsConstructor
public class PasswordRestoreService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Qualifier("emailPasswordRestoreSender")
    private final PasswordRestoreSender passwordRestoreSender;
    private final PasswordRestoreRepository passwordRestoreRepository;

    private String restoreToken;
    private User user;

    //    todo rate limit
    public void sendToken(PasswordResetRequest request)
    {
        User user = userRepository.findByEmail(request.getEmail());

        if (user == null) {
            throw new UserException("Пользователь не был найден");
        }

        this.user = user;

        this.checkQtyRequests();

        this.generateRestoreToken();

        this.saveAndSendRestoreToken();
    }

    private void checkQtyRequests()
    {
        // todo cleaner by cron
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1L);
        //        after - Позже определенной даты (>):
        //        todo проверить выборку
        Collection<PasswordRestore> rows = passwordRestoreRepository.findByUserIdAndCreatedAtAfter(this.user.getId(), yesterday);

        if (rows.isEmpty()) {
            return;
        }

        if (rows.size() > 3) {
            throw new RateLimitException("Превышен лимит запросов, используйте уже отправленные токены");
        } else {
            var lastPasswordRestore = rows.iterator().next();
            var duration = Duration.between(yesterday, lastPasswordRestore.getCreatedAt());

            if (rows.size() == 3 && (duration.getSeconds() * 60) < 30) {
                throw new RateLimitException("Код уже был отправлен, попробуйте позже");
            } else if (rows.size() == 2 && (duration.getSeconds() * 60) < 10) {
                throw new RateLimitException("Код уже был отправлен, попробуйте позже");
            }
        }
    }

    public void restore(PasswordResetConfirmRequest request)
    {
        String hashedToken = SecureHelper.getHashedValue(request.getToken());

        Optional<PasswordRestore> passwordRestoreObj = passwordRestoreRepository.findOneByTokenAndExpiredAtAfter(hashedToken, LocalDateTime.now());

        if (passwordRestoreObj.isEmpty())
            throw new EntityNotFound("Указан истекший или некорректный токен");

        this.confirmRestore(passwordRestoreObj.get(), request);
    }

    @Transactional
    private void confirmRestore(PasswordRestore passwordRestore, PasswordResetConfirmRequest request) {
        User user = passwordRestore.getUser();
        user.setPassword(ArgonEncoder.hashString(request.getPassword()));
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        passwordRestore.setExpiredAt(LocalDateTime.now());

        passwordRestoreRepository.save(passwordRestore);

        //todo refresh-token revoke by user
    }


    public void generateRestoreToken() {
        this.restoreToken = SecureHelper.getRandString(OptionalInt.of(32));
    }

    @Transactional
    private void saveAndSendRestoreToken()
    {
        PasswordRestore newPwdRestore = new PasswordRestore();
        newPwdRestore.setUser(this.user);
        newPwdRestore.setToken(SecureHelper.getHashedValue(this.restoreToken));
        newPwdRestore.setExpiredAt(LocalDateTime.now().plusMinutes(30L));

        passwordRestoreRepository.save(newPwdRestore);

        passwordRestoreSender.setToken(this.restoreToken);
        passwordRestoreSender.setUser(user);
        if (!passwordRestoreSender.send()) {
            throw new NotificationSendException("Токен для восстановления не удалось отправить");
        }
    }
}
