package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.FirmwareEntity;
import lwi.vision.repository.FirmwareRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FirmwareEntity}.
 */
@Service
@Transactional
public class FirmwareService {

    private final Logger log = LoggerFactory.getLogger(FirmwareService.class);

    private final FirmwareRepository firmwareRepository;

    public FirmwareService(FirmwareRepository firmwareRepository) {
        this.firmwareRepository = firmwareRepository;
    }

    /**
     * Save a firmware.
     *
     * @param firmwareEntity the entity to save.
     * @return the persisted entity.
     */
    public FirmwareEntity save(FirmwareEntity firmwareEntity) {
        log.debug("Request to save Firmware : {}", firmwareEntity);
        return firmwareRepository.save(firmwareEntity);
    }

    /**
     * Partially update a firmware.
     *
     * @param firmwareEntity the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FirmwareEntity> partialUpdate(FirmwareEntity firmwareEntity) {
        log.debug("Request to partially update Firmware : {}", firmwareEntity);

        return firmwareRepository
            .findById(firmwareEntity.getId())
            .map(
                existingFirmware -> {
                    if (firmwareEntity.getVersion() != null) {
                        existingFirmware.setVersion(firmwareEntity.getVersion());
                    }
                    if (firmwareEntity.getPath() != null) {
                        existingFirmware.setPath(firmwareEntity.getPath());
                    }

                    return existingFirmware;
                }
            )
            .map(firmwareRepository::save);
    }

    /**
     * Get all the firmware.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FirmwareEntity> findAll() {
        log.debug("Request to get all Firmware");
        return firmwareRepository.findAll();
    }

    /**
     * Get one firmware by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FirmwareEntity> findOne(Long id) {
        log.debug("Request to get Firmware : {}", id);
        return firmwareRepository.findById(id);
    }

    /**
     * Delete the firmware by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Firmware : {}", id);
        firmwareRepository.deleteById(id);
    }
}
