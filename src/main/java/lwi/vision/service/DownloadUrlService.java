package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.DownloadUrlEntity;
import lwi.vision.repository.DownloadUrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DownloadUrlEntity}.
 */
@Service
@Transactional
public class DownloadUrlService {

    private final Logger log = LoggerFactory.getLogger(DownloadUrlService.class);

    private final DownloadUrlRepository downloadUrlRepository;

    public DownloadUrlService(DownloadUrlRepository downloadUrlRepository) {
        this.downloadUrlRepository = downloadUrlRepository;
    }

    /**
     * Save a downloadUrl.
     *
     * @param downloadUrlEntity the entity to save.
     * @return the persisted entity.
     */
    public DownloadUrlEntity save(DownloadUrlEntity downloadUrlEntity) {
        log.debug("Request to save DownloadUrl : {}", downloadUrlEntity);
        return downloadUrlRepository.save(downloadUrlEntity);
    }

    /**
     * Partially update a downloadUrl.
     *
     * @param downloadUrlEntity the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DownloadUrlEntity> partialUpdate(DownloadUrlEntity downloadUrlEntity) {
        log.debug("Request to partially update DownloadUrl : {}", downloadUrlEntity);

        return downloadUrlRepository
            .findById(downloadUrlEntity.getId())
            .map(
                existingDownloadUrl -> {
                    if (downloadUrlEntity.getExpirationDate() != null) {
                        existingDownloadUrl.setExpirationDate(downloadUrlEntity.getExpirationDate());
                    }
                    if (downloadUrlEntity.getUrl() != null) {
                        existingDownloadUrl.setUrl(downloadUrlEntity.getUrl());
                    }

                    return existingDownloadUrl;
                }
            )
            .map(downloadUrlRepository::save);
    }

    /**
     * Get all the downloadUrls.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DownloadUrlEntity> findAll() {
        log.debug("Request to get all DownloadUrls");
        return downloadUrlRepository.findAll();
    }

    /**
     * Get one downloadUrl by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DownloadUrlEntity> findOne(Long id) {
        log.debug("Request to get DownloadUrl : {}", id);
        return downloadUrlRepository.findById(id);
    }

    /**
     * Delete the downloadUrl by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DownloadUrl : {}", id);
        downloadUrlRepository.deleteById(id);
    }
}
