package burunzhuy.helper;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;
import java.util.OptionalInt;

public class SecureHelper {
    public static String getRandString(OptionalInt length)
    {
        var secureRandom = new SecureRandom();

        int bytesNeeded = (int) Math.ceil(length.orElse(10) * 3.0 / 4.0);
        byte[] buffer = new byte[bytesNeeded];

        secureRandom.nextBytes(buffer);
        String rawString = Base64.getUrlEncoder().withoutPadding().encodeToString(buffer);
        return rawString.substring(0, length.orElse(10));
    }

    public static String getHashedValue(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            IO.println(e.getMessage());
            return "";
        }
    }
}
