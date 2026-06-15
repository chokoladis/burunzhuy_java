package burunzhuy.service;

import burunzhuy.entity.File;
import burunzhuy.repository.FileRepository;
import burunzhuy.tool.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${storage.upload-dir}")
    private String uploadDir;

    private final FileRepository fileRepository;

    public File save(MultipartFile file, String subDir)
    {
        String ext = this.getExt(file);
        String name = UUID.randomUUID() + "." + ext;
        Path filePath = Paths.get(this.uploadDir, subDir, name);

        try {
            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath);

            File newFile = new File();
            newFile.setOriginalName(file.getOriginalFilename());
            newFile.setPath(subDir + "/" + name);
            newFile.setExt(ext);

            return fileRepository.save(newFile);
        } catch (IOException e) {
            Logger.logToFile("storage.txt", e.getMessage());
            return null;
        } catch (Throwable e) {
            Logger.logToFile("storage.txt", e.getMessage());
            e.printStackTrace();

            try {
                Files.deleteIfExists(filePath);
            } catch (IOException ignored){}
        }

        return null;
    }

    public String getExt(MultipartFile file)
    {
        //todo file.getContentType()
        String name = file.getOriginalFilename();
        if (name.contains(".")){
            return name.substring(name.indexOf('.') + 1).toLowerCase();
        }

        return null;
    }

    //todo обработка после delete/update например в idea
    public boolean remove(File file)
    {
        Path filePath = Paths.get(this.uploadDir, file.getPath());

        try {
            Files.deleteIfExists(filePath);
            return true;
        } catch (Throwable e) {
            Logger.logToFile("storage.txt", e.getMessage());
            return false;
        }
    }
}
