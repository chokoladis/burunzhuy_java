package burunzhuy.dto.file;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
final public class FileDTO {

    @NotBlank
    @Size(max = 150)
    private String originalName;
    @NotBlank
    @Size(max = 500)
    private String path;
    @NotBlank
    @Size(max = 10)
    private String ext;
//    @Size(max = 50)
//    private String memoType;
}
