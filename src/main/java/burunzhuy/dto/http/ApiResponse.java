package burunzhuy.dto.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private T data;

    private List<String> errors;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(data, List.of());
    }

    public static <T> ApiResponse<T> error(String... errors) {
        return new ApiResponse<>(null, List.of(errors));
    }
}
