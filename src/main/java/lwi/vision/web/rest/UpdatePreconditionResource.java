package lwi.vision.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lwi.vision.domain.UpdatePreconditionEntity;
import lwi.vision.repository.UpdatePreconditionRepository;
import lwi.vision.service.UpdatePreconditionService;
import lwi.vision.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link lwi.vision.domain.UpdatePreconditionEntity}.
 */
@RestController
@RequestMapping("/api")
public class UpdatePreconditionResource {

    private final Logger log = LoggerFactory.getLogger(UpdatePreconditionResource.class);

    private static final String ENTITY_NAME = "updatePrecondition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UpdatePreconditionService updatePreconditionService;

    private final UpdatePreconditionRepository updatePreconditionRepository;

    public UpdatePreconditionResource(
        UpdatePreconditionService updatePreconditionService,
        UpdatePreconditionRepository updatePreconditionRepository
    ) {
        this.updatePreconditionService = updatePreconditionService;
        this.updatePreconditionRepository = updatePreconditionRepository;
    }

    /**
     * {@code POST  /update-preconditions} : Create a new updatePrecondition.
     *
     * @param updatePreconditionEntity the updatePreconditionEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new updatePreconditionEntity, or with status {@code 400 (Bad Request)} if the updatePrecondition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/update-preconditions")
    public ResponseEntity<UpdatePreconditionEntity> createUpdatePrecondition(
        @RequestBody UpdatePreconditionEntity updatePreconditionEntity
    ) throws URISyntaxException {
        log.debug("REST request to save UpdatePrecondition : {}", updatePreconditionEntity);
        if (updatePreconditionEntity.getId() != null) {
            throw new BadRequestAlertException("A new updatePrecondition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UpdatePreconditionEntity result = updatePreconditionService.save(updatePreconditionEntity);
        return ResponseEntity
            .created(new URI("/api/update-preconditions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /update-preconditions/:id} : Updates an existing updatePrecondition.
     *
     * @param id the id of the updatePreconditionEntity to save.
     * @param updatePreconditionEntity the updatePreconditionEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated updatePreconditionEntity,
     * or with status {@code 400 (Bad Request)} if the updatePreconditionEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the updatePreconditionEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/update-preconditions/{id}")
    public ResponseEntity<UpdatePreconditionEntity> updateUpdatePrecondition(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UpdatePreconditionEntity updatePreconditionEntity
    ) throws URISyntaxException {
        log.debug("REST request to update UpdatePrecondition : {}, {}", id, updatePreconditionEntity);
        if (updatePreconditionEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, updatePreconditionEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!updatePreconditionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UpdatePreconditionEntity result = updatePreconditionService.save(updatePreconditionEntity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, updatePreconditionEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /update-preconditions/:id} : Partial updates given fields of an existing updatePrecondition, field will ignore if it is null
     *
     * @param id the id of the updatePreconditionEntity to save.
     * @param updatePreconditionEntity the updatePreconditionEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated updatePreconditionEntity,
     * or with status {@code 400 (Bad Request)} if the updatePreconditionEntity is not valid,
     * or with status {@code 404 (Not Found)} if the updatePreconditionEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the updatePreconditionEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/update-preconditions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UpdatePreconditionEntity> partialUpdateUpdatePrecondition(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UpdatePreconditionEntity updatePreconditionEntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update UpdatePrecondition partially : {}, {}", id, updatePreconditionEntity);
        if (updatePreconditionEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, updatePreconditionEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!updatePreconditionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UpdatePreconditionEntity> result = updatePreconditionService.partialUpdate(updatePreconditionEntity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, updatePreconditionEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /update-preconditions} : get all the updatePreconditions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of updatePreconditions in body.
     */
    @GetMapping("/update-preconditions")
    public List<UpdatePreconditionEntity> getAllUpdatePreconditions() {
        log.debug("REST request to get all UpdatePreconditions");
        return updatePreconditionService.findAll();
    }

    /**
     * {@code GET  /update-preconditions/:id} : get the "id" updatePrecondition.
     *
     * @param id the id of the updatePreconditionEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updatePreconditionEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/update-preconditions/{id}")
    public ResponseEntity<UpdatePreconditionEntity> getUpdatePrecondition(@PathVariable Long id) {
        log.debug("REST request to get UpdatePrecondition : {}", id);
        Optional<UpdatePreconditionEntity> updatePreconditionEntity = updatePreconditionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(updatePreconditionEntity);
    }

    /**
     * {@code DELETE  /update-preconditions/:id} : delete the "id" updatePrecondition.
     *
     * @param id the id of the updatePreconditionEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/update-preconditions/{id}")
    public ResponseEntity<Void> deleteUpdatePrecondition(@PathVariable Long id) {
        log.debug("REST request to delete UpdatePrecondition : {}", id);
        updatePreconditionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
