package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.SoftwareUpdate;
import lwi.vision.repository.SoftwareUpdateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SoftwareUpdate}.
 */
@Service
@Transactional
public class SoftwareUpdateService {

    private final Logger log = LoggerFactory.getLogger(SoftwareUpdateService.class);

    private final SoftwareUpdateRepository softwareUpdateRepository;

    public SoftwareUpdateService(SoftwareUpdateRepository softwareUpdateRepository) {
        this.softwareUpdateRepository = softwareUpdateRepository;
    }

    /**
     * Save a softwareUpdate.
     *
     * @param softwareUpdate the entity to save.
     * @return the persisted entity.
     */
    public SoftwareUpdate save(SoftwareUpdate softwareUpdate) {
        log.debug("Request to save SoftwareUpdate : {}", softwareUpdate);
        return softwareUpdateRepository.save(softwareUpdate);
    }

    /**
     * Partially update a softwareUpdate.
     *
     * @param softwareUpdate the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SoftwareUpdate> partialUpdate(SoftwareUpdate softwareUpdate) {
        log.debug("Request to partially update SoftwareUpdate : {}", softwareUpdate);

        return softwareUpdateRepository
            .findById(softwareUpdate.getId())
            .map(
                existingSoftwareUpdate -> {
                    if (softwareUpdate.getActive() != null) {
                        existingSoftwareUpdate.setActive(softwareUpdate.getActive());
                    }

                    return existingSoftwareUpdate;
                }
            )
            .map(softwareUpdateRepository::save);
    }

    /**
     * Get all the softwareUpdates.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SoftwareUpdate> findAll() {
        log.debug("Request to get all SoftwareUpdates");
        return softwareUpdateRepository.findAll();
    }

    /**
     * Get one softwareUpdate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SoftwareUpdate> findOne(Long id) {
        log.debug("Request to get SoftwareUpdate : {}", id);
        return softwareUpdateRepository.findById(id);
    }

    /**
     * Delete the softwareUpdate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SoftwareUpdate : {}", id);
        softwareUpdateRepository.deleteById(id);
    }
}
