package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.SoftwareEntity;
import lwi.vision.repository.SoftwareRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SoftwareEntity}.
 */
@Service
@Transactional
public class SoftwareService {

    private final Logger log = LoggerFactory.getLogger(SoftwareService.class);

    private final SoftwareRepository softwareRepository;

    public SoftwareService(SoftwareRepository softwareRepository) {
        this.softwareRepository = softwareRepository;
    }

    /**
     * Save a software.
     *
     * @param softwareEntity the entity to save.
     * @return the persisted entity.
     */
    public SoftwareEntity save(SoftwareEntity softwareEntity) {
        log.debug("Request to save Software : {}", softwareEntity);
        return softwareRepository.save(softwareEntity);
    }

    /**
     * Partially update a software.
     *
     * @param softwareEntity the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SoftwareEntity> partialUpdate(SoftwareEntity softwareEntity) {
        log.debug("Request to partially update Software : {}", softwareEntity);

        return softwareRepository
            .findById(softwareEntity.getId())
            .map(
                existingSoftware -> {
                    if (softwareEntity.getVersion() != null) {
                        existingSoftware.setVersion(softwareEntity.getVersion());
                    }
                    if (softwareEntity.getPath() != null) {
                        existingSoftware.setPath(softwareEntity.getPath());
                    }

                    return existingSoftware;
                }
            )
            .map(softwareRepository::save);
    }

    /**
     * Get all the software.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SoftwareEntity> findAll() {
        log.debug("Request to get all Software");
        return softwareRepository.findAll();
    }

    /**
     * Get one software by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SoftwareEntity> findOne(Long id) {
        log.debug("Request to get Software : {}", id);
        return softwareRepository.findById(id);
    }

    /**
     * Delete the software by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Software : {}", id);
        softwareRepository.deleteById(id);
    }
}
