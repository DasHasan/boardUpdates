package lwi.vision.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lwi.vision.domain.BoardUpdateEntity;
import lwi.vision.domain.UpdateKeysEntity;
import lwi.vision.repository.BoardUpdateRepository;
import lwi.vision.service.BoardUpdateQueryService;
import lwi.vision.service.BoardUpdateService;
import lwi.vision.service.criteria.BoardUpdateCriteria;
import lwi.vision.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link lwi.vision.domain.BoardUpdateEntity}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BoardUpdateResource {

    private final Logger log = LoggerFactory.getLogger(BoardUpdateResource.class);

    private static final String ENTITY_NAME = "boardUpdate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoardUpdateService boardUpdateService;

    private final BoardUpdateRepository boardUpdateRepository;

    private final BoardUpdateQueryService boardUpdateQueryService;

    public BoardUpdateResource(
        BoardUpdateService boardUpdateService,
        BoardUpdateRepository boardUpdateRepository,
        BoardUpdateQueryService boardUpdateQueryService
    ) {
        this.boardUpdateService = boardUpdateService;
        this.boardUpdateRepository = boardUpdateRepository;
        this.boardUpdateQueryService = boardUpdateQueryService;
    }

    /**
     * {@code POST  /board-updates} : Create a new boardUpdate.
     *
     * @param boardUpdateEntity the boardUpdateEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new boardUpdateEntity, or with status {@code 400 (Bad Request)} if the boardUpdate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/board-updates")
    public ResponseEntity<BoardUpdateEntity> createBoardUpdate(@RequestBody BoardUpdateEntity boardUpdateEntity) throws URISyntaxException {
        log.debug("REST request to save BoardUpdate : {}", boardUpdateEntity);
        if (boardUpdateEntity.getId() != null) {
            throw new BadRequestAlertException("A new boardUpdate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BoardUpdateEntity result = boardUpdateService.save(boardUpdateEntity);
        return ResponseEntity
            .created(new URI("/api/board-updates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /board-updates/:id} : Updates an existing boardUpdate.
     *
     * @param id the id of the boardUpdateEntity to save.
     * @param boardUpdateEntity the boardUpdateEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boardUpdateEntity,
     * or with status {@code 400 (Bad Request)} if the boardUpdateEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the boardUpdateEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/board-updates/{id}")
    public ResponseEntity<BoardUpdateEntity> updateBoardUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BoardUpdateEntity boardUpdateEntity
    ) throws URISyntaxException {
        log.debug("REST request to update BoardUpdate : {}, {}", id, boardUpdateEntity);
        if (boardUpdateEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boardUpdateEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boardUpdateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BoardUpdateEntity result = boardUpdateService.save(boardUpdateEntity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boardUpdateEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /board-updates/:id} : Partial updates given fields of an existing boardUpdate, field will ignore if it is null
     *
     * @param id the id of the boardUpdateEntity to save.
     * @param boardUpdateEntity the boardUpdateEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boardUpdateEntity,
     * or with status {@code 400 (Bad Request)} if the boardUpdateEntity is not valid,
     * or with status {@code 404 (Not Found)} if the boardUpdateEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the boardUpdateEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/board-updates/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BoardUpdateEntity> partialUpdateBoardUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BoardUpdateEntity boardUpdateEntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update BoardUpdate partially : {}, {}", id, boardUpdateEntity);
        if (boardUpdateEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boardUpdateEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boardUpdateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BoardUpdateEntity> result = boardUpdateService.partialUpdate(boardUpdateEntity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boardUpdateEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /board-updates} : get all the boardUpdates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boardUpdates in body.
     */
    @GetMapping("/board-updates")
    public ResponseEntity<List<BoardUpdateEntity>> getAllBoardUpdates(BoardUpdateCriteria criteria) {
        log.debug("REST request to get BoardUpdates by criteria: {}", criteria);
        List<BoardUpdateEntity> entityList = boardUpdateQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /board-updates/count} : count all the boardUpdates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/board-updates/count")
    public ResponseEntity<Long> countBoardUpdates(BoardUpdateCriteria criteria) {
        log.debug("REST request to count BoardUpdates by criteria: {}", criteria);
        return ResponseEntity.ok().body(boardUpdateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /board-updates/:id} : get the "id" boardUpdate.
     *
     * @param id the id of the boardUpdateEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the boardUpdateEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/board-updates/{id}")
    public ResponseEntity<BoardUpdateEntity> getBoardUpdate(@PathVariable Long id) {
        log.debug("REST request to get BoardUpdate : {}", id);
        Optional<BoardUpdateEntity> boardUpdateEntity = boardUpdateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(boardUpdateEntity);
    }

    /**
     * {@code DELETE  /board-updates/:id} : delete the "id" boardUpdate.
     *
     * @param id the id of the boardUpdateEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/board-updates/{id}")
    public ResponseEntity<Void> deleteBoardUpdate(@PathVariable Long id) {
        log.debug("REST request to delete BoardUpdate : {}", id);
        boardUpdateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
