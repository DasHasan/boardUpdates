package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.UpdateVersionPreconditionEntity;
import lwi.vision.repository.UpdateVersionPreconditionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UpdateVersionPreconditionEntity}.
 */
@Service
@Transactional
public class UpdateVersionPreconditionService {

    private final Logger log = LoggerFactory.getLogger(UpdateVersionPreconditionService.class);

    private final UpdateVersionPreconditionRepository updateVersionPreconditionRepository;

    public UpdateVersionPreconditionService(UpdateVersionPreconditionRepository updateVersionPreconditionRepository) {
        this.updateVersionPreconditionRepository = updateVersionPreconditionRepository;
    }

    /**
     * Save a updateVersionPrecondition.
     *
     * @param updateVersionPreconditionEntity the entity to save.
     * @return the persisted entity.
     */
    public UpdateVersionPreconditionEntity save(UpdateVersionPreconditionEntity updateVersionPreconditionEntity) {
        log.debug("Request to save UpdateVersionPrecondition : {}", updateVersionPreconditionEntity);
        return updateVersionPreconditionRepository.save(updateVersionPreconditionEntity);
    }

    /**
     * Partially update a updateVersionPrecondition.
     *
     * @param updateVersionPreconditionEntity the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UpdateVersionPreconditionEntity> partialUpdate(UpdateVersionPreconditionEntity updateVersionPreconditionEntity) {
        log.debug("Request to partially update UpdateVersionPrecondition : {}", updateVersionPreconditionEntity);

        return updateVersionPreconditionRepository
            .findById(updateVersionPreconditionEntity.getId())
            .map(
                existingUpdateVersionPrecondition -> {
                    if (updateVersionPreconditionEntity.getVersion() != null) {
                        existingUpdateVersionPrecondition.setVersion(updateVersionPreconditionEntity.getVersion());
                    }

                    return existingUpdateVersionPrecondition;
                }
            )
            .map(updateVersionPreconditionRepository::save);
    }

    /**
     * Get all the updateVersionPreconditions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UpdateVersionPreconditionEntity> findAll() {
        log.debug("Request to get all UpdateVersionPreconditions");
        return updateVersionPreconditionRepository.findAll();
    }

    /**
     * Get one updateVersionPrecondition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UpdateVersionPreconditionEntity> findOne(Long id) {
        log.debug("Request to get UpdateVersionPrecondition : {}", id);
        return updateVersionPreconditionRepository.findById(id);
    }

    /**
     * Delete the updateVersionPrecondition by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UpdateVersionPrecondition : {}", id);
        updateVersionPreconditionRepository.deleteById(id);
    }
}
