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

        Path targetFile = targetDir.resolve(file.getOriginalFilename());

        Files.copy(file.getInputStream(), targetFile);
        return targetFile.toString();
    }

    @GetMapping("/a")
    public String a() {
        return "a";
    }
}
