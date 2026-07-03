package burunzhuy.dto.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RefreshRequest(
    @JsonProperty("refresh_token") String refresh_token
) {}