package burunzhuy.resource;

import burunzhuy.entity.File;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"id", "originalName", "path", "ext"})
@EqualsAndHashCode
public class FileResource {
    private final Long id;
    private final String originalName;
    private final String path;
    private final String ext;
//    private String memoType = null;
//    private FilePurpose purpose;

    public FileResource(File file) {
        this.id = file.getId();
        this.originalName = file.getOriginalName();
        this.path = file.getPath();
        this.ext = file.getExt();
//        this.memoType = file.getMemoType();
    }
}
