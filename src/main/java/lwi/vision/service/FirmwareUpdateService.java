package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.FirmwareUpdate;
import lwi.vision.repository.FirmwareUpdateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FirmwareUpdate}.
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
     * @param firmwareUpdate the entity to save.
     * @return the persisted entity.
     */
    public FirmwareUpdate save(FirmwareUpdate firmwareUpdate) {
        log.debug("Request to save FirmwareUpdate : {}", firmwareUpdate);
        return firmwareUpdateRepository.save(firmwareUpdate);
    }

    /**
     * Partially update a firmwareUpdate.
     *
     * @param firmwareUpdate the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FirmwareUpdate> partialUpdate(FirmwareUpdate firmwareUpdate) {
        log.debug("Request to partially update FirmwareUpdate : {}", firmwareUpdate);

        return firmwareUpdateRepository
            .findById(firmwareUpdate.getId())
            .map(
                existingFirmwareUpdate -> {
                    if (firmwareUpdate.getActive() != null) {
                        existingFirmwareUpdate.setActive(firmwareUpdate.getActive());
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
    public List<FirmwareUpdate> findAll() {
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
    public Optional<FirmwareUpdate> findOne(Long id) {
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
