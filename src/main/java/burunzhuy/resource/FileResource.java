package burunzhuy.resource;

import burunzhuy.entity.File;
import lombok.Getter;

@Getter
public class FileResource {
    private Long id;
    private String originalName;
    private String path;
    private String memoType = null;
//    private FilePurpose purpose;

    public FileResource(File file)
    {
        this.id = file.getId();
        this.originalName = file.getOriginalName();
        this.path = file.getPath();
        this.memoType = file.getMemoType();
    }
}
