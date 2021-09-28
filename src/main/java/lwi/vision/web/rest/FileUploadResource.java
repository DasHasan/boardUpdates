package lwi.vision.web.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import lwi.vision.service.EmailAlreadyUsedException;
import lwi.vision.web.rest.errors.BadRequestAlertException;
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

    /**
     * POST uploadSoftware
     *
     * @return
     */
    @PostMapping("/upload-software")
    public ResponseEntity<Map<String, String>> uploadSoftware(@RequestBody MultipartFile file) {
        Path target;
        try {
            target = Paths.get("./software-updates").resolve(file.getOriginalFilename());
            if (!target.toFile().exists()) {
                Files.copy(file.getInputStream(), target);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestAlertException("Entity not found", "ENTITY_NAME", "idnotfound");
        }

        Map<String, String> body = new HashMap<>();
        body.put("path", target.toAbsolutePath().toString());
        return ResponseEntity.ok(body);
    }

    /**
     * POST uploadFirmware
     */
    @PostMapping("/upload-firmware")
    public String uploadFirmware() {
        return "uploadFirmware";
    }

    /**
     * GET a
     */
    @GetMapping("/a")
    public String a() {
        return "a";
    }
}
