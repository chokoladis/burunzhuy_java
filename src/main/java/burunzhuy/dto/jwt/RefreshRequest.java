package burunzhuy.dto.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;

public record RefreshRequest(
    @Size(min = 20)
    @JsonProperty("refresh_token")
    String refresh_token
) {}