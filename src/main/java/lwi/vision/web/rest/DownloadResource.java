package lwi.vision.web.rest;

import lwi.vision.service.DownloadService;
import lwi.vision.service.EmailAlreadyUsedException;
import lwi.vision.web.rest.errors.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * DownloadResource controller
 */
@RestController
@RequestMapping("/download")
public class DownloadResource {

    private final Logger log = LoggerFactory.getLogger(DownloadResource.class);
    private final DownloadService downloadService;

    public DownloadResource(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    /**
     * GET download
     *
     * @return
     * @param id
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public FileSystemResource download(@PathVariable Long id) {
        FileSystemResource resource = new FileSystemResource(downloadService.getDownload(id));
        if (!resource.exists()) {
            throw new ServiceException();
        }
        return resource;
    }
}