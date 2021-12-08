package lwi.vision.web.rest;

import lwi.vision.domain.BoardUpdateEntity;
import lwi.vision.domain.DownloadUrlEntity;
import lwi.vision.repository.DownloadUrlRepository;
import lwi.vision.service.BoardUpdateService;
import lwi.vision.service.ServiceException;
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

    private final DownloadUrlRepository downloadUrlRepository;

    public FileDownloadResource(BoardUpdateService boardUpdateService, DownloadUrlRepository downloadUrlRepository) {
        this.boardUpdateService = boardUpdateService;
        this.downloadUrlRepository = downloadUrlRepository;
    }

    /**
     * GET download
     *
     * @return File to download
     * @param id of the BoardUpdate
     */
    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public FileSystemResource downloadById(@PathVariable Long id) {
        FileSystemResource resource = new FileSystemResource(
            boardUpdateService.findOne(id).orElseThrow(() -> new ServiceException("Datei nicht gefunden")).getPath()
        );
        if (!resource.exists()) {
            throw new lwi.vision.service.ServiceException("Datei nicht gefunden");
        }
        return resource;
    }

    /**
     * GET download
     *
     * @return File to download
     * @param uuid of the BoardUpdate URL
     */
    @GetMapping(value = "/{uuid}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public FileSystemResource downloadByUuid(@PathVariable String uuid) {
        BoardUpdateEntity boardUpdate = downloadUrlRepository
            .findFirstByUrl(uuid)
            .map(DownloadUrlEntity::getBoardUpdate)
            .orElseThrow(ServiceException::fileNotFound);
        FileSystemResource resource = new FileSystemResource(boardUpdate.getPath());
        if (!resource.exists()) {
            throw new lwi.vision.service.ServiceException("Datei nicht gefunden");
        }
        return resource;
    }
}
