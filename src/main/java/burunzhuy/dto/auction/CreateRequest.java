package burunzhuy.dto.auction;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
final public class CreateRequest {

    @NotBlank
    private Long ideaId;

    @Future
    private LocalDateTime finishedAt;
}
