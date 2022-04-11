package lwi.vision.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lwi.vision.domain.UpdateVersionPreconditionEntity;
import lwi.vision.repository.UpdateVersionPreconditionRepository;
import lwi.vision.service.UpdateVersionPreconditionService;
import lwi.vision.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link lwi.vision.domain.UpdateVersionPreconditionEntity}.
 */
@RestController
@RequestMapping("/api")
public class UpdateVersionPreconditionResource {

    private final Logger log = LoggerFactory.getLogger(UpdateVersionPreconditionResource.class);

    private static final String ENTITY_NAME = "updateVersionPrecondition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UpdateVersionPreconditionService updateVersionPreconditionService;

    private final UpdateVersionPreconditionRepository updateVersionPreconditionRepository;

    public UpdateVersionPreconditionResource(
        UpdateVersionPreconditionService updateVersionPreconditionService,
        UpdateVersionPreconditionRepository updateVersionPreconditionRepository
    ) {
        this.updateVersionPreconditionService = updateVersionPreconditionService;
        this.updateVersionPreconditionRepository = updateVersionPreconditionRepository;
    }

    /**
     * {@code POST  /update-version-preconditions} : Create a new updateVersionPrecondition.
     *
     * @param updateVersionPreconditionEntity the updateVersionPreconditionEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new updateVersionPreconditionEntity, or with status {@code 400 (Bad Request)} if the updateVersionPrecondition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/update-version-preconditions")
    public ResponseEntity<UpdateVersionPreconditionEntity> createUpdateVersionPrecondition(
        @RequestBody UpdateVersionPreconditionEntity updateVersionPreconditionEntity
    ) throws URISyntaxException {
        log.debug("REST request to save UpdateVersionPrecondition : {}", updateVersionPreconditionEntity);
        if (updateVersionPreconditionEntity.getId() != null) {
            throw new BadRequestAlertException("A new updateVersionPrecondition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UpdateVersionPreconditionEntity result = updateVersionPreconditionService.save(updateVersionPreconditionEntity);
        return ResponseEntity
            .created(new URI("/api/update-version-preconditions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /update-version-preconditions/:id} : Updates an existing updateVersionPrecondition.
     *
     * @param id the id of the updateVersionPreconditionEntity to save.
     * @param updateVersionPreconditionEntity the updateVersionPreconditionEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated updateVersionPreconditionEntity,
     * or with status {@code 400 (Bad Request)} if the updateVersionPreconditionEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the updateVersionPreconditionEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/update-version-preconditions/{id}")
    public ResponseEntity<UpdateVersionPreconditionEntity> updateUpdateVersionPrecondition(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UpdateVersionPreconditionEntity updateVersionPreconditionEntity
    ) throws URISyntaxException {
        log.debug("REST request to update UpdateVersionPrecondition : {}, {}", id, updateVersionPreconditionEntity);
        if (updateVersionPreconditionEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, updateVersionPreconditionEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!updateVersionPreconditionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UpdateVersionPreconditionEntity result = updateVersionPreconditionService.save(updateVersionPreconditionEntity);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, updateVersionPreconditionEntity.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /update-version-preconditions/:id} : Partial updates given fields of an existing updateVersionPrecondition, field will ignore if it is null
     *
     * @param id the id of the updateVersionPreconditionEntity to save.
     * @param updateVersionPreconditionEntity the updateVersionPreconditionEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated updateVersionPreconditionEntity,
     * or with status {@code 400 (Bad Request)} if the updateVersionPreconditionEntity is not valid,
     * or with status {@code 404 (Not Found)} if the updateVersionPreconditionEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the updateVersionPreconditionEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/update-version-preconditions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UpdateVersionPreconditionEntity> partialUpdateUpdateVersionPrecondition(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UpdateVersionPreconditionEntity updateVersionPreconditionEntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update UpdateVersionPrecondition partially : {}, {}", id, updateVersionPreconditionEntity);
        if (updateVersionPreconditionEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, updateVersionPreconditionEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!updateVersionPreconditionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UpdateVersionPreconditionEntity> result = updateVersionPreconditionService.partialUpdate(updateVersionPreconditionEntity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, updateVersionPreconditionEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /update-version-preconditions} : get all the updateVersionPreconditions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of updateVersionPreconditions in body.
     */
    @GetMapping("/update-version-preconditions")
    public List<UpdateVersionPreconditionEntity> getAllUpdateVersionPreconditions() {
        log.debug("REST request to get all UpdateVersionPreconditions");
        return updateVersionPreconditionService.findAll();
    }

    /**
     * {@code GET  /update-version-preconditions/:id} : get the "id" updateVersionPrecondition.
     *
     * @param id the id of the updateVersionPreconditionEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updateVersionPreconditionEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/update-version-preconditions/{id}")
    public ResponseEntity<UpdateVersionPreconditionEntity> getUpdateVersionPrecondition(@PathVariable Long id) {
        log.debug("REST request to get UpdateVersionPrecondition : {}", id);
        Optional<UpdateVersionPreconditionEntity> updateVersionPreconditionEntity = updateVersionPreconditionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(updateVersionPreconditionEntity);
    }

    /**
     * {@code DELETE  /update-version-preconditions/:id} : delete the "id" updateVersionPrecondition.
     *
     * @param id the id of the updateVersionPreconditionEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/update-version-preconditions/{id}")
    public ResponseEntity<Void> deleteUpdateVersionPrecondition(@PathVariable Long id) {
        log.debug("REST request to delete UpdateVersionPrecondition : {}", id);
        updateVersionPreconditionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
