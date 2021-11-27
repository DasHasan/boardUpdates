package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.DownloadEntity;
import lwi.vision.repository.DownloadRepository;
import lwi.vision.web.rest.errors.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DownloadEntity}.
 */
@Service
@Transactional
public class DownloadService {

    private final Logger log = LoggerFactory.getLogger(DownloadService.class);

    private final DownloadRepository downloadRepository;

    private final BoardUpdateService boardUpdateService;

    public DownloadService(DownloadRepository downloadRepository, BoardUpdateService boardUpdateService) {
        this.downloadRepository = downloadRepository;
        this.boardUpdateService = boardUpdateService;
    }

    /**
     * Save a download.
     *
     * @param downloadEntity the entity to save.
     * @return the persisted entity.
     */
    public DownloadEntity save(DownloadEntity downloadEntity) {
        log.debug("Request to save Download : {}", downloadEntity);
        return downloadRepository.save(downloadEntity);
    }

    /**
     * Partially update a download.
     *
     * @param downloadEntity the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DownloadEntity> partialUpdate(DownloadEntity downloadEntity) {
        log.debug("Request to partially update Download : {}", downloadEntity);

        return downloadRepository
            .findById(downloadEntity.getId())
            .map(
                existingDownload -> {
                    if (downloadEntity.getDate() != null) {
                        existingDownload.setDate(downloadEntity.getDate());
                    }

                    return existingDownload;
                }
            )
            .map(downloadRepository::save);
    }

    /**
     * Get all the downloads.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DownloadEntity> findAll() {
        log.debug("Request to get all Downloads");
        return downloadRepository.findAll();
    }

    /**
     * Get one download by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DownloadEntity> findOne(Long id) {
        log.debug("Request to get Download : {}", id);
        return downloadRepository.findById(id);
    }

    /**
     * Delete the download by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Download : {}", id);
        downloadRepository.deleteById(id);
    }

    public String getDownload(Long id) {
        return boardUpdateService.findOne(id).orElseThrow(ServiceException::new).getPath();
    }
}
