package burunzhuy.resource;

import burunzhuy.entity.File;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"id", "originalName", "path", "ext"})
public class FileResource {
    private Long id;
    private String originalName;
    private String path;
    private String ext;
//    private String memoType = null;
//    private FilePurpose purpose;

    public FileResource(File file)
    {
        this.id = file.getId();
        this.originalName = file.getOriginalName();
        this.path = file.getPath();
        this.ext = file.getExt();
//        this.memoType = file.getMemoType();
    }
}
