package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.Firmware;
import lwi.vision.repository.FirmwareRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Firmware}.
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
     * @param firmware the entity to save.
     * @return the persisted entity.
     */
    public Firmware save(Firmware firmware) {
        log.debug("Request to save Firmware : {}", firmware);
        return firmwareRepository.save(firmware);
    }

    /**
     * Partially update a firmware.
     *
     * @param firmware the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Firmware> partialUpdate(Firmware firmware) {
        log.debug("Request to partially update Firmware : {}", firmware);

        return firmwareRepository
            .findById(firmware.getId())
            .map(
                existingFirmware -> {
                    if (firmware.getVersion() != null) {
                        existingFirmware.setVersion(firmware.getVersion());
                    }
                    if (firmware.getPath() != null) {
                        existingFirmware.setPath(firmware.getPath());
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
    public List<Firmware> findAll() {
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
    public Optional<Firmware> findOne(Long id) {
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
