package lwi.vision.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lwi.vision.domain.BoardUpdateSuccessorEntity;
import lwi.vision.repository.BoardUpdateSuccessorRepository;
import lwi.vision.service.BoardUpdateSuccessorQueryService;
import lwi.vision.service.BoardUpdateSuccessorService;
import lwi.vision.service.criteria.BoardUpdateSuccessorCriteria;
import lwi.vision.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link lwi.vision.domain.BoardUpdateSuccessorEntity}.
 */
@RestController
@RequestMapping("/api")
public class BoardUpdateSuccessorResource {

    private final Logger log = LoggerFactory.getLogger(BoardUpdateSuccessorResource.class);

    private static final String ENTITY_NAME = "boardUpdateSuccessor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoardUpdateSuccessorService boardUpdateSuccessorService;

    private final BoardUpdateSuccessorRepository boardUpdateSuccessorRepository;

    private final BoardUpdateSuccessorQueryService boardUpdateSuccessorQueryService;

    public BoardUpdateSuccessorResource(
        BoardUpdateSuccessorService boardUpdateSuccessorService,
        BoardUpdateSuccessorRepository boardUpdateSuccessorRepository,
        BoardUpdateSuccessorQueryService boardUpdateSuccessorQueryService
    ) {
        this.boardUpdateSuccessorService = boardUpdateSuccessorService;
        this.boardUpdateSuccessorRepository = boardUpdateSuccessorRepository;
        this.boardUpdateSuccessorQueryService = boardUpdateSuccessorQueryService;
    }

    /**
     * {@code POST  /board-update-successors} : Create a new boardUpdateSuccessor.
     *
     * @param boardUpdateSuccessorEntity the boardUpdateSuccessorEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new boardUpdateSuccessorEntity, or with status {@code 400 (Bad Request)} if the boardUpdateSuccessor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/board-update-successors")
    public ResponseEntity<BoardUpdateSuccessorEntity> createBoardUpdateSuccessor(
        @RequestBody BoardUpdateSuccessorEntity boardUpdateSuccessorEntity
    ) throws URISyntaxException {
        log.debug("REST request to save BoardUpdateSuccessor : {}", boardUpdateSuccessorEntity);
        if (boardUpdateSuccessorEntity.getId() != null) {
            throw new BadRequestAlertException("A new boardUpdateSuccessor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BoardUpdateSuccessorEntity result = boardUpdateSuccessorService.save(boardUpdateSuccessorEntity);
        return ResponseEntity
            .created(new URI("/api/board-update-successors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /board-update-successors/:id} : Updates an existing boardUpdateSuccessor.
     *
     * @param id the id of the boardUpdateSuccessorEntity to save.
     * @param boardUpdateSuccessorEntity the boardUpdateSuccessorEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boardUpdateSuccessorEntity,
     * or with status {@code 400 (Bad Request)} if the boardUpdateSuccessorEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the boardUpdateSuccessorEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/board-update-successors/{id}")
    public ResponseEntity<BoardUpdateSuccessorEntity> updateBoardUpdateSuccessor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BoardUpdateSuccessorEntity boardUpdateSuccessorEntity
    ) throws URISyntaxException {
        log.debug("REST request to update BoardUpdateSuccessor : {}, {}", id, boardUpdateSuccessorEntity);
        if (boardUpdateSuccessorEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boardUpdateSuccessorEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boardUpdateSuccessorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BoardUpdateSuccessorEntity result = boardUpdateSuccessorService.save(boardUpdateSuccessorEntity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boardUpdateSuccessorEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /board-update-successors/:id} : Partial updates given fields of an existing boardUpdateSuccessor, field will ignore if it is null
     *
     * @param id the id of the boardUpdateSuccessorEntity to save.
     * @param boardUpdateSuccessorEntity the boardUpdateSuccessorEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boardUpdateSuccessorEntity,
     * or with status {@code 400 (Bad Request)} if the boardUpdateSuccessorEntity is not valid,
     * or with status {@code 404 (Not Found)} if the boardUpdateSuccessorEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the boardUpdateSuccessorEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/board-update-successors/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BoardUpdateSuccessorEntity> partialUpdateBoardUpdateSuccessor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BoardUpdateSuccessorEntity boardUpdateSuccessorEntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update BoardUpdateSuccessor partially : {}, {}", id, boardUpdateSuccessorEntity);
        if (boardUpdateSuccessorEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boardUpdateSuccessorEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boardUpdateSuccessorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BoardUpdateSuccessorEntity> result = boardUpdateSuccessorService.partialUpdate(boardUpdateSuccessorEntity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boardUpdateSuccessorEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /board-update-successors} : get all the boardUpdateSuccessors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boardUpdateSuccessors in body.
     */
    @GetMapping("/board-update-successors")
    public ResponseEntity<List<BoardUpdateSuccessorEntity>> getAllBoardUpdateSuccessors(BoardUpdateSuccessorCriteria criteria) {
        log.debug("REST request to get BoardUpdateSuccessors by criteria: {}", criteria);
        List<BoardUpdateSuccessorEntity> entityList = boardUpdateSuccessorQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /board-update-successors/count} : count all the boardUpdateSuccessors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/board-update-successors/count")
    public ResponseEntity<Long> countBoardUpdateSuccessors(BoardUpdateSuccessorCriteria criteria) {
        log.debug("REST request to count BoardUpdateSuccessors by criteria: {}", criteria);
        return ResponseEntity.ok().body(boardUpdateSuccessorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /board-update-successors/:id} : get the "id" boardUpdateSuccessor.
     *
     * @param id the id of the boardUpdateSuccessorEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the boardUpdateSuccessorEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/board-update-successors/{id}")
    public ResponseEntity<BoardUpdateSuccessorEntity> getBoardUpdateSuccessor(@PathVariable Long id) {
        log.debug("REST request to get BoardUpdateSuccessor : {}", id);
        Optional<BoardUpdateSuccessorEntity> boardUpdateSuccessorEntity = boardUpdateSuccessorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(boardUpdateSuccessorEntity);
    }

    /**
     * {@code DELETE  /board-update-successors/:id} : delete the "id" boardUpdateSuccessor.
     *
     * @param id the id of the boardUpdateSuccessorEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/board-update-successors/{id}")
    public ResponseEntity<Void> deleteBoardUpdateSuccessor(@PathVariable Long id) {
        log.debug("REST request to delete BoardUpdateSuccessor : {}", id);
        boardUpdateSuccessorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
