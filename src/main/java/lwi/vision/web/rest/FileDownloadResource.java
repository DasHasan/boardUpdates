package lwi.vision.web.rest;

import lwi.vision.domain.BoardUpdateEntity;
import lwi.vision.domain.DownloadUrlEntity;
import lwi.vision.repository.DownloadUrlRepository;
import lwi.vision.service.BoardUpdateService;
import lwi.vision.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
     * @param id of the BoardUpdate
     * @return File to download
     */
    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<FileSystemResource> downloadById(@PathVariable Long id) {
        BoardUpdateEntity boardUpdate = boardUpdateService.findOne(id).orElseThrow(ServiceException::updateNotFound);
        return buildResponse(boardUpdate);
    }

    /**
     * GET download
     *
     * @param uuid of the BoardUpdate URL
     * @return File to download
     */
    @GetMapping(value = "/{uuid}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<FileSystemResource> downloadByUuid(@PathVariable String uuid) {
        BoardUpdateEntity boardUpdate = downloadUrlRepository
            .findFirstByUrl(uuid)
            .map(DownloadUrlEntity::getBoardUpdate)
            .orElseThrow(ServiceException::updateNotFound);
        return buildResponse(boardUpdate);
    }

    private ResponseEntity<FileSystemResource> buildResponse(BoardUpdateEntity boardUpdate) {
        try {
            FileSystemResource resource = new FileSystemResource(boardUpdate.getPath());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(
                ContentDisposition.attachment().filename("autoupdate_" + boardUpdate.getVersion() + ".zip").build()
            );
            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (Exception e) {
            throw ServiceException.fileNotFound();
        }
    }
}
