package lwi.vision.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lwi.vision.domain.SoftwareUpdate;
import lwi.vision.repository.SoftwareUpdateRepository;
import lwi.vision.service.SoftwareUpdateService;
import lwi.vision.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link lwi.vision.domain.SoftwareUpdate}.
 */
@RestController
@RequestMapping("/api")
public class SoftwareUpdateResource {

    private final Logger log = LoggerFactory.getLogger(SoftwareUpdateResource.class);

    private static final String ENTITY_NAME = "softwareUpdate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SoftwareUpdateService softwareUpdateService;

    private final SoftwareUpdateRepository softwareUpdateRepository;

    public SoftwareUpdateResource(SoftwareUpdateService softwareUpdateService, SoftwareUpdateRepository softwareUpdateRepository) {
        this.softwareUpdateService = softwareUpdateService;
        this.softwareUpdateRepository = softwareUpdateRepository;
    }

    /**
     * {@code POST  /software-updates} : Create a new softwareUpdate.
     *
     * @param softwareUpdate the softwareUpdate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new softwareUpdate, or with status {@code 400 (Bad Request)} if the softwareUpdate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/software-updates")
    public ResponseEntity<SoftwareUpdate> createSoftwareUpdate(@RequestBody SoftwareUpdate softwareUpdate) throws URISyntaxException {
        log.debug("REST request to save SoftwareUpdate : {}", softwareUpdate);
        if (softwareUpdate.getId() != null) {
            throw new BadRequestAlertException("A new softwareUpdate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SoftwareUpdate result = softwareUpdateService.save(softwareUpdate);
        return ResponseEntity
            .created(new URI("/api/software-updates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /software-updates/:id} : Updates an existing softwareUpdate.
     *
     * @param id the id of the softwareUpdate to save.
     * @param softwareUpdate the softwareUpdate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated softwareUpdate,
     * or with status {@code 400 (Bad Request)} if the softwareUpdate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the softwareUpdate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/software-updates/{id}")
    public ResponseEntity<SoftwareUpdate> updateSoftwareUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SoftwareUpdate softwareUpdate
    ) throws URISyntaxException {
        log.debug("REST request to update SoftwareUpdate : {}, {}", id, softwareUpdate);
        if (softwareUpdate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, softwareUpdate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!softwareUpdateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SoftwareUpdate result = softwareUpdateService.save(softwareUpdate);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, softwareUpdate.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /software-updates/:id} : Partial updates given fields of an existing softwareUpdate, field will ignore if it is null
     *
     * @param id the id of the softwareUpdate to save.
     * @param softwareUpdate the softwareUpdate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated softwareUpdate,
     * or with status {@code 400 (Bad Request)} if the softwareUpdate is not valid,
     * or with status {@code 404 (Not Found)} if the softwareUpdate is not found,
     * or with status {@code 500 (Internal Server Error)} if the softwareUpdate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/software-updates/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SoftwareUpdate> partialUpdateSoftwareUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SoftwareUpdate softwareUpdate
    ) throws URISyntaxException {
        log.debug("REST request to partial update SoftwareUpdate partially : {}, {}", id, softwareUpdate);
        if (softwareUpdate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, softwareUpdate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!softwareUpdateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SoftwareUpdate> result = softwareUpdateService.partialUpdate(softwareUpdate);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, softwareUpdate.getId().toString())
        );
    }

    /**
     * {@code GET  /software-updates} : get all the softwareUpdates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of softwareUpdates in body.
     */
    @GetMapping("/software-updates")
    public List<SoftwareUpdate> getAllSoftwareUpdates() {
        log.debug("REST request to get all SoftwareUpdates");
        return softwareUpdateService.findAll();
    }

    /**
     * {@code GET  /software-updates/:id} : get the "id" softwareUpdate.
     *
     * @param id the id of the softwareUpdate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the softwareUpdate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/software-updates/{id}")
    public ResponseEntity<SoftwareUpdate> getSoftwareUpdate(@PathVariable Long id) {
        log.debug("REST request to get SoftwareUpdate : {}", id);
        Optional<SoftwareUpdate> softwareUpdate = softwareUpdateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(softwareUpdate);
    }

    /**
     * {@code DELETE  /software-updates/:id} : delete the "id" softwareUpdate.
     *
     * @param id the id of the softwareUpdate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/software-updates/{id}")
    public ResponseEntity<Void> deleteSoftwareUpdate(@PathVariable Long id) {
        log.debug("REST request to delete SoftwareUpdate : {}", id);
        softwareUpdateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
