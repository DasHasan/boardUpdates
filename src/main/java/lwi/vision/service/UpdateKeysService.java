package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.UpdateKeysEntity;
import lwi.vision.repository.UpdateKeysRepository;
import lwi.vision.repository.UpdatePreconditionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UpdateKeysEntity}.
 */
@Service
@Transactional
public class UpdateKeysService {

    private final Logger log = LoggerFactory.getLogger(UpdateKeysService.class);

    private final UpdateKeysRepository updateKeysRepository;

    private final UpdatePreconditionRepository updatePreconditionRepository;

    public UpdateKeysService(UpdateKeysRepository updateKeysRepository, UpdatePreconditionRepository updatePreconditionRepository) {
        this.updateKeysRepository = updateKeysRepository;
        this.updatePreconditionRepository = updatePreconditionRepository;
    }

    /**
     * Save a updateKeys.
     *
     * @param updateKeysEntity the entity to save.
     * @return the persisted entity.
     */
    public UpdateKeysEntity save(UpdateKeysEntity updateKeysEntity) {
        log.debug("Request to save UpdateKeys : {}", updateKeysEntity);
        UpdateKeysEntity updateKeys = updateKeysRepository.save(updateKeysEntity);
        if (updateKeysEntity.getUpdatePrecondition() != null && updateKeysEntity.getUpdatePrecondition().getId() != null) {
            updatePreconditionRepository.findById(updateKeysEntity.getUpdatePrecondition().getId()).ifPresent(updatePreconditionEntity -> {
                updatePreconditionEntity.getUpdateKeys().add(updateKeys);
                updatePreconditionRepository.save(updatePreconditionEntity);
            });
        }
        return updateKeys;
    }

    /**
     * Partially update a updateKeys.
     *
     * @param updateKeysEntity the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UpdateKeysEntity> partialUpdate(UpdateKeysEntity updateKeysEntity) {
        log.debug("Request to partially update UpdateKeys : {}", updateKeysEntity);

        return updateKeysRepository
            .findById(updateKeysEntity.getId())
            .map(
                existingUpdateKeys -> {
                    if (updateKeysEntity.getKey() != null) {
                        existingUpdateKeys.setKey(updateKeysEntity.getKey());
                    }

                    return existingUpdateKeys;
                }
            )
            .map(updateKeysRepository::save);
    }

    /**
     * Get all the updateKeys.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UpdateKeysEntity> findAll() {
        log.debug("Request to get all UpdateKeys");
        return updateKeysRepository.findAll();
    }

    /**
     * Get one updateKeys by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UpdateKeysEntity> findOne(Long id) {
        log.debug("Request to get UpdateKeys : {}", id);
        return updateKeysRepository.findById(id);
    }

    /**
     * Delete the updateKeys by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UpdateKeys : {}", id);
        updateKeysRepository.deleteById(id);
    }
}
