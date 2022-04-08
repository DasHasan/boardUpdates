package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.UpdatePreconditionEntity;
import lwi.vision.repository.UpdatePreconditionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UpdatePreconditionEntity}.
 */
@Service
@Transactional
public class UpdatePreconditionService {

    private final Logger log = LoggerFactory.getLogger(UpdatePreconditionService.class);

    private final UpdatePreconditionRepository updatePreconditionRepository;

    public UpdatePreconditionService(UpdatePreconditionRepository updatePreconditionRepository) {
        this.updatePreconditionRepository = updatePreconditionRepository;
    }

    /**
     * Save a updatePrecondition.
     *
     * @param updatePreconditionEntity the entity to save.
     * @return the persisted entity.
     */
    public UpdatePreconditionEntity save(UpdatePreconditionEntity updatePreconditionEntity) {
        log.debug("Request to save UpdatePrecondition : {}", updatePreconditionEntity);
        return updatePreconditionRepository.save(updatePreconditionEntity);
    }

    /**
     * Partially update a updatePrecondition.
     *
     * @param updatePreconditionEntity the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UpdatePreconditionEntity> partialUpdate(UpdatePreconditionEntity updatePreconditionEntity) {
        log.debug("Request to partially update UpdatePrecondition : {}", updatePreconditionEntity);

        return updatePreconditionRepository
            .findById(updatePreconditionEntity.getId())
            .map(
                existingUpdatePrecondition -> {
                    return existingUpdatePrecondition;
                }
            )
            .map(updatePreconditionRepository::save);
    }

    /**
     * Get all the updatePreconditions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UpdatePreconditionEntity> findAll() {
        log.debug("Request to get all UpdatePreconditions");
        return updatePreconditionRepository.findAll();
    }

    /**
     * Get one updatePrecondition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UpdatePreconditionEntity> findOne(Long id) {
        log.debug("Request to get UpdatePrecondition : {}", id);
        return updatePreconditionRepository.findById(id);
    }

    /**
     * Delete the updatePrecondition by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UpdatePrecondition : {}", id);
        updatePreconditionRepository.deleteById(id);
    }
}
