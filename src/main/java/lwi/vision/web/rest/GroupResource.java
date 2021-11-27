package lwi.vision.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lwi.vision.domain.GroupEntity;
import lwi.vision.repository.GroupRepository;
import lwi.vision.service.GroupQueryService;
import lwi.vision.service.GroupService;
import lwi.vision.service.criteria.GroupCriteria;
import lwi.vision.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link lwi.vision.domain.GroupEntity}.
 */
@RestController
@RequestMapping("/api")
public class GroupResource {

    private final Logger log = LoggerFactory.getLogger(GroupResource.class);

    private static final String ENTITY_NAME = "group";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GroupService groupService;

    private final GroupRepository groupRepository;

    private final GroupQueryService groupQueryService;

    public GroupResource(GroupService groupService, GroupRepository groupRepository, GroupQueryService groupQueryService) {
        this.groupService = groupService;
        this.groupRepository = groupRepository;
        this.groupQueryService = groupQueryService;
    }

    /**
     * {@code POST  /groups} : Create a new group.
     *
     * @param groupEntity the groupEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new groupEntity, or with status {@code 400 (Bad Request)} if the group has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/groups")
    public ResponseEntity<GroupEntity> createGroup(@RequestBody GroupEntity groupEntity) throws URISyntaxException {
        log.debug("REST request to save Group : {}", groupEntity);
        if (groupEntity.getId() != null) {
            throw new BadRequestAlertException("A new group cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GroupEntity result = groupService.save(groupEntity);
        return ResponseEntity
            .created(new URI("/api/groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /groups/:id} : Updates an existing group.
     *
     * @param id the id of the groupEntity to save.
     * @param groupEntity the groupEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupEntity,
     * or with status {@code 400 (Bad Request)} if the groupEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the groupEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/groups/{id}")
    public ResponseEntity<GroupEntity> updateGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GroupEntity groupEntity
    ) throws URISyntaxException {
        log.debug("REST request to update Group : {}, {}", id, groupEntity);
        if (groupEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groupEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!groupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GroupEntity result = groupService.save(groupEntity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /groups/:id} : Partial updates given fields of an existing group, field will ignore if it is null
     *
     * @param id the id of the groupEntity to save.
     * @param groupEntity the groupEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupEntity,
     * or with status {@code 400 (Bad Request)} if the groupEntity is not valid,
     * or with status {@code 404 (Not Found)} if the groupEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the groupEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/groups/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<GroupEntity> partialUpdateGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GroupEntity groupEntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update Group partially : {}, {}", id, groupEntity);
        if (groupEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groupEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!groupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GroupEntity> result = groupService.partialUpdate(groupEntity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /groups} : get all the groups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groups in body.
     */
    @GetMapping("/groups")
    public ResponseEntity<List<GroupEntity>> getAllGroups(GroupCriteria criteria) {
        log.debug("REST request to get Groups by criteria: {}", criteria);
        List<GroupEntity> entityList = groupQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /groups/count} : count all the groups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/groups/count")
    public ResponseEntity<Long> countGroups(GroupCriteria criteria) {
        log.debug("REST request to count Groups by criteria: {}", criteria);
        return ResponseEntity.ok().body(groupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /groups/:id} : get the "id" group.
     *
     * @param id the id of the groupEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the groupEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/groups/{id}")
    public ResponseEntity<GroupEntity> getGroup(@PathVariable Long id) {
        log.debug("REST request to get Group : {}", id);
        Optional<GroupEntity> groupEntity = groupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(groupEntity);
    }

    /**
     * {@code DELETE  /groups/:id} : delete the "id" group.
     *
     * @param id the id of the groupEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        log.debug("REST request to delete Group : {}", id);
        groupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
