package lwi.vision.web.rest;

import lwi.vision.service.BoardUpdateService;
import lwi.vision.web.rest.errors.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * FileDownloadResource controller
 */
@RestController
@RequestMapping("/download")
public class FileDownloadResource {

    private final Logger log = LoggerFactory.getLogger(FileDownloadResource.class);
    private final BoardUpdateService boardUpdateService;

    public FileDownloadResource(BoardUpdateService boardUpdateService) {
        this.boardUpdateService = boardUpdateService;
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
        FileSystemResource resource = new FileSystemResource(boardUpdateService.findOne(id).orElseThrow(ServiceException::new).getPath());
        if (!resource.exists()) {
            throw new ServiceException();
        }
        return resource;
    }
}
