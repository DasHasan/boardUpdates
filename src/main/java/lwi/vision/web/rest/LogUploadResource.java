package lwi.vision.web.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileUploadResource controller
 */
@RestController
@RequestMapping("/api/log-upload")
public class LogUploadResource {

    private final Logger log = LoggerFactory.getLogger(LogUploadResource.class);

    @PostMapping("/")
    public ResponseEntity<Map<String, String>> uploadLogFile(
        @RequestParam MultipartFile file,
        @RequestParam String serial,
        @RequestParam String version
    ) throws IOException {
        Map<String, String> body = new HashMap<>();
        body.put("path", saveFile(file, ""));
        return ResponseEntity.ok(body);
    }

    private String saveFile(MultipartFile file, String path) throws IOException {
        Path targetDir = Paths.get("logs", "versions", path);
        Files.createDirectories(targetDir);

        Path targetFile = targetDir.resolve("runtime.log");

        Files.copy(file.getInputStream(), targetFile);
        return targetFile.toString();
    }
}
