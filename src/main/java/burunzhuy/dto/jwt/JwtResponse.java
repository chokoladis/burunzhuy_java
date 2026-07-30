package burunzhuy.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
final public class JwtResponse {
    public String accessToken;
    public String refreshToken;
}
