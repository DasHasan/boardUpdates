package lwi.vision.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lwi.vision.domain.UpdateKeysEntity;
import lwi.vision.repository.UpdateKeysRepository;
import lwi.vision.service.UpdateKeysQueryService;
import lwi.vision.service.UpdateKeysService;
import lwi.vision.service.criteria.UpdateKeysCriteria;
import lwi.vision.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link lwi.vision.domain.UpdateKeysEntity}.
 */
@RestController
@RequestMapping("/api")
public class UpdateKeysResource {

    private final Logger log = LoggerFactory.getLogger(UpdateKeysResource.class);

    private static final String ENTITY_NAME = "updateKeys";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UpdateKeysService updateKeysService;

    private final UpdateKeysRepository updateKeysRepository;

    private final UpdateKeysQueryService updateKeysQueryService;

    public UpdateKeysResource(
        UpdateKeysService updateKeysService,
        UpdateKeysRepository updateKeysRepository,
        UpdateKeysQueryService updateKeysQueryService
    ) {
        this.updateKeysService = updateKeysService;
        this.updateKeysRepository = updateKeysRepository;
        this.updateKeysQueryService = updateKeysQueryService;
    }

    /**
     * {@code POST  /update-keys} : Create a new updateKeys.
     *
     * @param updateKeysEntity the updateKeysEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new updateKeysEntity, or with status {@code 400 (Bad Request)} if the updateKeys has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/update-keys")
    public ResponseEntity<UpdateKeysEntity> createUpdateKeys(@RequestBody UpdateKeysEntity updateKeysEntity) throws URISyntaxException {
        log.debug("REST request to save UpdateKeys : {}", updateKeysEntity);
        if (updateKeysEntity.getId() != null) {
            throw new BadRequestAlertException("A new updateKeys cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UpdateKeysEntity result = updateKeysService.save(updateKeysEntity);
        return ResponseEntity
            .created(new URI("/api/update-keys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /update-keys/:id} : Updates an existing updateKeys.
     *
     * @param id the id of the updateKeysEntity to save.
     * @param updateKeysEntity the updateKeysEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated updateKeysEntity,
     * or with status {@code 400 (Bad Request)} if the updateKeysEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the updateKeysEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/update-keys/{id}")
    public ResponseEntity<UpdateKeysEntity> updateUpdateKeys(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UpdateKeysEntity updateKeysEntity
    ) throws URISyntaxException {
        log.debug("REST request to update UpdateKeys : {}, {}", id, updateKeysEntity);
        if (updateKeysEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, updateKeysEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!updateKeysRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UpdateKeysEntity result = updateKeysService.save(updateKeysEntity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, updateKeysEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /update-keys/:id} : Partial updates given fields of an existing updateKeys, field will ignore if it is null
     *
     * @param id the id of the updateKeysEntity to save.
     * @param updateKeysEntity the updateKeysEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated updateKeysEntity,
     * or with status {@code 400 (Bad Request)} if the updateKeysEntity is not valid,
     * or with status {@code 404 (Not Found)} if the updateKeysEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the updateKeysEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/update-keys/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UpdateKeysEntity> partialUpdateUpdateKeys(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UpdateKeysEntity updateKeysEntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update UpdateKeys partially : {}, {}", id, updateKeysEntity);
        if (updateKeysEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, updateKeysEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!updateKeysRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UpdateKeysEntity> result = updateKeysService.partialUpdate(updateKeysEntity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, updateKeysEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /update-keys} : get all the updateKeys.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of updateKeys in body.
     */
    @GetMapping("/update-keys")
    public ResponseEntity<List<UpdateKeysEntity>> getAllUpdateKeys(UpdateKeysCriteria criteria) {
        log.debug("REST request to get UpdateKeys by criteria: {}", criteria);
        List<UpdateKeysEntity> entityList = updateKeysQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /update-keys/count} : count all the updateKeys.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/update-keys/count")
    public ResponseEntity<Long> countUpdateKeys(UpdateKeysCriteria criteria) {
        log.debug("REST request to count UpdateKeys by criteria: {}", criteria);
        return ResponseEntity.ok().body(updateKeysQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /update-keys/:id} : get the "id" updateKeys.
     *
     * @param id the id of the updateKeysEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updateKeysEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/update-keys/{id}")
    public ResponseEntity<UpdateKeysEntity> getUpdateKeys(@PathVariable Long id) {
        log.debug("REST request to get UpdateKeys : {}", id);
        Optional<UpdateKeysEntity> updateKeysEntity = updateKeysService.findOne(id);
        return ResponseUtil.wrapOrNotFound(updateKeysEntity);
    }

    /**
     * {@code DELETE  /update-keys/:id} : delete the "id" updateKeys.
     *
     * @param id the id of the updateKeysEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/update-keys/{id}")
    public ResponseEntity<Void> deleteUpdateKeys(@PathVariable Long id) {
        log.debug("REST request to delete UpdateKeys : {}", id);
        updateKeysService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
