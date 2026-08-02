package burunzhuy.helper;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserHelper {
    @Value("${env.value}")
    private String env;

    public String getIpAddress(HttpServletRequest httpRequest)
    {
        return env.equals("prod") ? httpRequest.getRemoteAddr() : httpRequest.getLocalAddr();
    }

    public static String getDeviceFingerprint(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String acceptLanguage = request.getHeader("Accept-Language");
        String acceptEncoding = request.getHeader("Accept-Encoding");

        String raw = String.join("|",
                userAgent != null ? userAgent : "",
                acceptLanguage != null ? acceptLanguage : "",
                acceptEncoding != null ? acceptEncoding : "");

        return SecureHelper.getHashedValue(raw);
    }
}
