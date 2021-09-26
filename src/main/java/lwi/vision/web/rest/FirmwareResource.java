package lwi.vision.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lwi.vision.domain.FirmwareEntity;
import lwi.vision.repository.FirmwareRepository;
import lwi.vision.service.FirmwareService;
import lwi.vision.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link lwi.vision.domain.FirmwareEntity}.
 */
@RestController
@RequestMapping("/api")
public class FirmwareResource {

    private final Logger log = LoggerFactory.getLogger(FirmwareResource.class);

    private static final String ENTITY_NAME = "firmware";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FirmwareService firmwareService;

    private final FirmwareRepository firmwareRepository;

    public FirmwareResource(FirmwareService firmwareService, FirmwareRepository firmwareRepository) {
        this.firmwareService = firmwareService;
        this.firmwareRepository = firmwareRepository;
    }

    /**
     * {@code POST  /firmware} : Create a new firmware.
     *
     * @param firmwareEntity the firmwareEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new firmwareEntity, or with status {@code 400 (Bad Request)} if the firmware has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/firmware")
    public ResponseEntity<FirmwareEntity> createFirmware(@RequestBody FirmwareEntity firmwareEntity) throws URISyntaxException {
        log.debug("REST request to save Firmware : {}", firmwareEntity);
        if (firmwareEntity.getId() != null) {
            throw new BadRequestAlertException("A new firmware cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FirmwareEntity result = firmwareService.save(firmwareEntity);
        return ResponseEntity
            .created(new URI("/api/firmware/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /firmware/:id} : Updates an existing firmware.
     *
     * @param id the id of the firmwareEntity to save.
     * @param firmwareEntity the firmwareEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated firmwareEntity,
     * or with status {@code 400 (Bad Request)} if the firmwareEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the firmwareEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/firmware/{id}")
    public ResponseEntity<FirmwareEntity> updateFirmware(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FirmwareEntity firmwareEntity
    ) throws URISyntaxException {
        log.debug("REST request to update Firmware : {}, {}", id, firmwareEntity);
        if (firmwareEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, firmwareEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!firmwareRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FirmwareEntity result = firmwareService.save(firmwareEntity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, firmwareEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /firmware/:id} : Partial updates given fields of an existing firmware, field will ignore if it is null
     *
     * @param id the id of the firmwareEntity to save.
     * @param firmwareEntity the firmwareEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated firmwareEntity,
     * or with status {@code 400 (Bad Request)} if the firmwareEntity is not valid,
     * or with status {@code 404 (Not Found)} if the firmwareEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the firmwareEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/firmware/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FirmwareEntity> partialUpdateFirmware(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FirmwareEntity firmwareEntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update Firmware partially : {}, {}", id, firmwareEntity);
        if (firmwareEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, firmwareEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!firmwareRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FirmwareEntity> result = firmwareService.partialUpdate(firmwareEntity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, firmwareEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /firmware} : get all the firmware.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of firmware in body.
     */
    @GetMapping("/firmware")
    public List<FirmwareEntity> getAllFirmware() {
        log.debug("REST request to get all Firmware");
        return firmwareService.findAll();
    }

    /**
     * {@code GET  /firmware/:id} : get the "id" firmware.
     *
     * @param id the id of the firmwareEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the firmwareEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/firmware/{id}")
    public ResponseEntity<FirmwareEntity> getFirmware(@PathVariable Long id) {
        log.debug("REST request to get Firmware : {}", id);
        Optional<FirmwareEntity> firmwareEntity = firmwareService.findOne(id);
        return ResponseUtil.wrapOrNotFound(firmwareEntity);
    }

    /**
     * {@code DELETE  /firmware/:id} : delete the "id" firmware.
     *
     * @param id the id of the firmwareEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/firmware/{id}")
    public ResponseEntity<Void> deleteFirmware(@PathVariable Long id) {
        log.debug("REST request to delete Firmware : {}", id);
        firmwareService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
