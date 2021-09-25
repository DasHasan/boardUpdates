package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.Software;
import lwi.vision.repository.SoftwareRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Software}.
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
     * @param software the entity to save.
     * @return the persisted entity.
     */
    public Software save(Software software) {
        log.debug("Request to save Software : {}", software);
        return softwareRepository.save(software);
    }

    /**
     * Partially update a software.
     *
     * @param software the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Software> partialUpdate(Software software) {
        log.debug("Request to partially update Software : {}", software);

        return softwareRepository
            .findById(software.getId())
            .map(
                existingSoftware -> {
                    if (software.getVersion() != null) {
                        existingSoftware.setVersion(software.getVersion());
                    }
                    if (software.getPath() != null) {
                        existingSoftware.setPath(software.getPath());
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
    public List<Software> findAll() {
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
    public Optional<Software> findOne(Long id) {
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
