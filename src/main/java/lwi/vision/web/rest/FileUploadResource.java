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

    @PostMapping("/upload-software")
    public ResponseEntity<Map<String, String>> uploadSoftware(
        @RequestParam String boardSerial,
        @RequestParam String version,
        @RequestParam MultipartFile file
    ) throws IOException {
        Map<String, String> body = new HashMap<>();
        body.put("path", saveFile(file, boardSerial, "software", version));
        return ResponseEntity.ok(body);
    }

    @PostMapping("/upload-firmware")
    public ResponseEntity<Map<String, String>> uploadFirmware(
        @RequestParam String boardSerial,
        @RequestParam String version,
        @RequestParam MultipartFile file
    ) throws IOException {
        Map<String, String> body = new HashMap<>();
        body.put("path", saveFile(file, boardSerial, "firmware", version));
        return ResponseEntity.ok(body);
    }

    private String saveFile(MultipartFile file, String serial, String updateType, String version) throws IOException {
        Path targetDir = Paths.get("updates", "versions", serial, updateType, version);
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
