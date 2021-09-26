package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.SoftwareUpdateEntity;
import lwi.vision.repository.SoftwareUpdateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SoftwareUpdateEntity}.
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
     * @param softwareUpdateEntity the entity to save.
     * @return the persisted entity.
     */
    public SoftwareUpdateEntity save(SoftwareUpdateEntity softwareUpdateEntity) {
        log.debug("Request to save SoftwareUpdate : {}", softwareUpdateEntity);
        return softwareUpdateRepository.save(softwareUpdateEntity);
    }

    /**
     * Partially update a softwareUpdate.
     *
     * @param softwareUpdateEntity the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SoftwareUpdateEntity> partialUpdate(SoftwareUpdateEntity softwareUpdateEntity) {
        log.debug("Request to partially update SoftwareUpdate : {}", softwareUpdateEntity);

        return softwareUpdateRepository
            .findById(softwareUpdateEntity.getId())
            .map(
                existingSoftwareUpdate -> {
                    if (softwareUpdateEntity.getActive() != null) {
                        existingSoftwareUpdate.setActive(softwareUpdateEntity.getActive());
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
    public List<SoftwareUpdateEntity> findAll() {
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
    public Optional<SoftwareUpdateEntity> findOne(Long id) {
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
