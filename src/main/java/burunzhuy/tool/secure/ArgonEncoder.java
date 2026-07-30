package burunzhuy.tool.secure;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class ArgonEncoder {
    private static final Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(16,32,3, 65536, 2);

    public static String hashString(String input) {
        return encoder.encode(input);
    }

    public static boolean isValueVerify(String input, String hashedValue) {
        return encoder.matches(input, hashedValue);
    }
}
