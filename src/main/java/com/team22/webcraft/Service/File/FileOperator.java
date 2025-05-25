package com.team22.webcraft.Service.File;

import com.team22.webcraft.DTO.MapData.MapProvideDTO;
import com.team22.webcraft.DTO.MapData.MapRemoveDTO;
import com.team22.webcraft.DTO.MapData.MapSaveDTO;
import com.team22.webcraft.DTO.MapData.MapUpdateDTO;
import com.team22.webcraft.Exception.MapData.FileEmptyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

@Component
public class FileOperator {
    @Value("${file.upload-dir}")
    private String uploadDir;

    public void saveFile(Long userDataId, MultipartFile file, MapSaveDTO mapSaveDTO) throws Exception {
        if (file.isEmpty()) {
            throw new FileEmptyException("There is no file to upload");
        }

        try {
            // Create external directory if it does not exist
            File directory = new File(uploadDir);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            // make file name
            String originalName = file.getOriginalFilename();
            String fileName = userDataId + mapSaveDTO.getMapName();
            String extension = originalName.substring(originalName.lastIndexOf("."));
            String fullFileName = fileName + extension;

            // set file location
            File destination = new File(directory, fullFileName);
            file.transferTo(destination);
        } catch (IOException e) {
            throw new IOException("File upload exception occur");
        }
    }

    public void updateFile(Long userDataId, MultipartFile file, MapUpdateDTO mapUpdateDTO, String prevName) throws Exception {
        if (file.isEmpty()) {
            throw new FileEmptyException("There is no file to update");
        }

        try {
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String originalName = file.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf("."));

            String prevFileName = userDataId + prevName + extension;
            String newFileName = userDataId + mapUpdateDTO.getNewMapName() + extension;

            // prev file path
            File targetFile = new File(directory, prevFileName);
            File newFile = new File(directory, newFileName);

            // delete prev file and make new file
            if (targetFile.exists())
                targetFile.delete();// delete prev file

            file.transferTo(newFile);// new file save
        } catch (IOException e) {
            throw new IOException("File update exception occur");
        }
    }

    public void removeFile(Long userDataId, MapRemoveDTO mapRemoveDTO) throws IOException {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String extension = ".txt";
        String fileName = userDataId + mapRemoveDTO.getMapName() + extension;

        // file path
        File targetFile = new File(directory, fileName);

        if (targetFile.exists())
            targetFile.delete();// delete file
    }

    public ResponseEntity<Resource> provideMap(Long userDataId, MapProvideDTO mapProvideDTO) throws Exception {
        String fileName = userDataId + mapProvideDTO.getMapName() + ".txt";
        File file = new File(uploadDir, fileName);

        if (!file.exists())
            throw new FileNotFoundException("File not found: " + fileName);

        Path path = file.toPath();
        Resource resource = new UrlResource(path.toUri());

        String contentDisposition = "attachment; filename=\"" + file.getName() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
