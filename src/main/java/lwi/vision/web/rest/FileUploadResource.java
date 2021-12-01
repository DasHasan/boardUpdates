package lwi.vision.web.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import lwi.vision.web.rest.errors.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileUploadResource controller
 */
@RestController
@RequestMapping("/api/file-upload")
public class FileUploadResource {

    private final Logger log = LoggerFactory.getLogger(FileUploadResource.class);

    @PostMapping("/")
    public ResponseEntity<Map<String, String>> uploadSoftware(@RequestParam MultipartFile file, @RequestParam String path)
        throws IOException {
        Map<String, String> body = new HashMap<>();
        body.put("path", saveFile(file, path));
        return ResponseEntity.ok(body);
    }

    private String saveFile(MultipartFile file, String path) throws IOException {
        Path targetDir = Paths.get("updates", "versions", path);
        Files.createDirectories(targetDir);

        Path targetPath = targetDir.resolve(file.getOriginalFilename());

        if (targetPath.toFile().exists()) {
            throw new lwi.vision.service.ServiceException("Datei existiert bereits");
            //            try {
            //                boolean delete = targetPath.toFile().delete();
            //                if (!delete) {
            //                    throw new Exception();
            //                }
            //            } catch (Exception e) {
            //                throw new lwi.vision.service.ServiceException("Datei konnte nicht ersetzt werden");
            //            }
        }

        Files.copy(file.getInputStream(), targetPath);
        return targetPath.toString();
    }
}
