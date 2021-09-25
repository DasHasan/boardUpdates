package lwi.vision.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lwi.vision.domain.FirmwareUpdate;
import lwi.vision.repository.FirmwareUpdateRepository;
import lwi.vision.service.FirmwareUpdateService;
import lwi.vision.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link lwi.vision.domain.FirmwareUpdate}.
 */
@RestController
@RequestMapping("/api")
public class FirmwareUpdateResource {

    private final Logger log = LoggerFactory.getLogger(FirmwareUpdateResource.class);

    private static final String ENTITY_NAME = "firmwareUpdate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FirmwareUpdateService firmwareUpdateService;

    private final FirmwareUpdateRepository firmwareUpdateRepository;

    public FirmwareUpdateResource(FirmwareUpdateService firmwareUpdateService, FirmwareUpdateRepository firmwareUpdateRepository) {
        this.firmwareUpdateService = firmwareUpdateService;
        this.firmwareUpdateRepository = firmwareUpdateRepository;
    }

    /**
     * {@code POST  /firmware-updates} : Create a new firmwareUpdate.
     *
     * @param firmwareUpdate the firmwareUpdate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new firmwareUpdate, or with status {@code 400 (Bad Request)} if the firmwareUpdate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/firmware-updates")
    public ResponseEntity<FirmwareUpdate> createFirmwareUpdate(@RequestBody FirmwareUpdate firmwareUpdate) throws URISyntaxException {
        log.debug("REST request to save FirmwareUpdate : {}", firmwareUpdate);
        if (firmwareUpdate.getId() != null) {
            throw new BadRequestAlertException("A new firmwareUpdate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FirmwareUpdate result = firmwareUpdateService.save(firmwareUpdate);
        return ResponseEntity
            .created(new URI("/api/firmware-updates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /firmware-updates/:id} : Updates an existing firmwareUpdate.
     *
     * @param id the id of the firmwareUpdate to save.
     * @param firmwareUpdate the firmwareUpdate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated firmwareUpdate,
     * or with status {@code 400 (Bad Request)} if the firmwareUpdate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the firmwareUpdate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/firmware-updates/{id}")
    public ResponseEntity<FirmwareUpdate> updateFirmwareUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FirmwareUpdate firmwareUpdate
    ) throws URISyntaxException {
        log.debug("REST request to update FirmwareUpdate : {}, {}", id, firmwareUpdate);
        if (firmwareUpdate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, firmwareUpdate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!firmwareUpdateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FirmwareUpdate result = firmwareUpdateService.save(firmwareUpdate);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, firmwareUpdate.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /firmware-updates/:id} : Partial updates given fields of an existing firmwareUpdate, field will ignore if it is null
     *
     * @param id the id of the firmwareUpdate to save.
     * @param firmwareUpdate the firmwareUpdate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated firmwareUpdate,
     * or with status {@code 400 (Bad Request)} if the firmwareUpdate is not valid,
     * or with status {@code 404 (Not Found)} if the firmwareUpdate is not found,
     * or with status {@code 500 (Internal Server Error)} if the firmwareUpdate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/firmware-updates/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FirmwareUpdate> partialUpdateFirmwareUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FirmwareUpdate firmwareUpdate
    ) throws URISyntaxException {
        log.debug("REST request to partial update FirmwareUpdate partially : {}, {}", id, firmwareUpdate);
        if (firmwareUpdate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, firmwareUpdate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!firmwareUpdateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FirmwareUpdate> result = firmwareUpdateService.partialUpdate(firmwareUpdate);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, firmwareUpdate.getId().toString())
        );
    }

    /**
     * {@code GET  /firmware-updates} : get all the firmwareUpdates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of firmwareUpdates in body.
     */
    @GetMapping("/firmware-updates")
    public List<FirmwareUpdate> getAllFirmwareUpdates() {
        log.debug("REST request to get all FirmwareUpdates");
        return firmwareUpdateService.findAll();
    }

    /**
     * {@code GET  /firmware-updates/:id} : get the "id" firmwareUpdate.
     *
     * @param id the id of the firmwareUpdate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the firmwareUpdate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/firmware-updates/{id}")
    public ResponseEntity<FirmwareUpdate> getFirmwareUpdate(@PathVariable Long id) {
        log.debug("REST request to get FirmwareUpdate : {}", id);
        Optional<FirmwareUpdate> firmwareUpdate = firmwareUpdateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(firmwareUpdate);
    }

    /**
     * {@code DELETE  /firmware-updates/:id} : delete the "id" firmwareUpdate.
     *
     * @param id the id of the firmwareUpdate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/firmware-updates/{id}")
    public ResponseEntity<Void> deleteFirmwareUpdate(@PathVariable Long id) {
        log.debug("REST request to delete FirmwareUpdate : {}", id);
        firmwareUpdateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
