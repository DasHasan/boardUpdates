package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.FirmwareUpdateEntity;
import lwi.vision.repository.FirmwareUpdateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FirmwareUpdateEntity}.
 */
@Service
@Transactional
public class FirmwareUpdateService {

    private final Logger log = LoggerFactory.getLogger(FirmwareUpdateService.class);

    private final FirmwareUpdateRepository firmwareUpdateRepository;

    public FirmwareUpdateService(FirmwareUpdateRepository firmwareUpdateRepository) {
        this.firmwareUpdateRepository = firmwareUpdateRepository;
    }

    /**
     * Save a firmwareUpdate.
     *
     * @param firmwareUpdateEntity the entity to save.
     * @return the persisted entity.
     */
    public FirmwareUpdateEntity save(FirmwareUpdateEntity firmwareUpdateEntity) {
        log.debug("Request to save FirmwareUpdate : {}", firmwareUpdateEntity);
        return firmwareUpdateRepository.save(firmwareUpdateEntity);
    }

    /**
     * Partially update a firmwareUpdate.
     *
     * @param firmwareUpdateEntity the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FirmwareUpdateEntity> partialUpdate(FirmwareUpdateEntity firmwareUpdateEntity) {
        log.debug("Request to partially update FirmwareUpdate : {}", firmwareUpdateEntity);

        return firmwareUpdateRepository
            .findById(firmwareUpdateEntity.getId())
            .map(
                existingFirmwareUpdate -> {
                    if (firmwareUpdateEntity.getActive() != null) {
                        existingFirmwareUpdate.setActive(firmwareUpdateEntity.getActive());
                    }

                    return existingFirmwareUpdate;
                }
            )
            .map(firmwareUpdateRepository::save);
    }

    /**
     * Get all the firmwareUpdates.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FirmwareUpdateEntity> findAll() {
        log.debug("Request to get all FirmwareUpdates");
        return firmwareUpdateRepository.findAll();
    }

    /**
     * Get one firmwareUpdate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FirmwareUpdateEntity> findOne(Long id) {
        log.debug("Request to get FirmwareUpdate : {}", id);
        return firmwareUpdateRepository.findById(id);
    }

    /**
     * Delete the firmwareUpdate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FirmwareUpdate : {}", id);
        firmwareUpdateRepository.deleteById(id);
    }
}
