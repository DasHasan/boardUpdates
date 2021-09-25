package lwi.vision.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lwi.vision.domain.Board;
import lwi.vision.repository.BoardRepository;
import lwi.vision.service.BoardService;
import lwi.vision.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link lwi.vision.domain.Board}.
 */
@RestController
@RequestMapping("/api")
public class BoardResource {

    private final Logger log = LoggerFactory.getLogger(BoardResource.class);

    private static final String ENTITY_NAME = "board";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoardService boardService;

    private final BoardRepository boardRepository;

    public BoardResource(BoardService boardService, BoardRepository boardRepository) {
        this.boardService = boardService;
        this.boardRepository = boardRepository;
    }

    /**
     * {@code POST  /boards} : Create a new board.
     *
     * @param board the board to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new board, or with status {@code 400 (Bad Request)} if the board has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/boards")
    public ResponseEntity<Board> createBoard(@RequestBody Board board) throws URISyntaxException {
        log.debug("REST request to save Board : {}", board);
        if (board.getId() != null) {
            throw new BadRequestAlertException("A new board cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Board result = boardService.save(board);
        return ResponseEntity
            .created(new URI("/api/boards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /boards/:id} : Updates an existing board.
     *
     * @param id the id of the board to save.
     * @param board the board to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated board,
     * or with status {@code 400 (Bad Request)} if the board is not valid,
     * or with status {@code 500 (Internal Server Error)} if the board couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/boards/{id}")
    public ResponseEntity<Board> updateBoard(@PathVariable(value = "id", required = false) final Long id, @RequestBody Board board)
        throws URISyntaxException {
        log.debug("REST request to update Board : {}, {}", id, board);
        if (board.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, board.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Board result = boardService.save(board);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, board.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /boards/:id} : Partial updates given fields of an existing board, field will ignore if it is null
     *
     * @param id the id of the board to save.
     * @param board the board to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated board,
     * or with status {@code 400 (Bad Request)} if the board is not valid,
     * or with status {@code 404 (Not Found)} if the board is not found,
     * or with status {@code 500 (Internal Server Error)} if the board couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/boards/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Board> partialUpdateBoard(@PathVariable(value = "id", required = false) final Long id, @RequestBody Board board)
        throws URISyntaxException {
        log.debug("REST request to partial update Board partially : {}, {}", id, board);
        if (board.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, board.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Board> result = boardService.partialUpdate(board);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, board.getId().toString())
        );
    }

    /**
     * {@code GET  /boards} : get all the boards.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boards in body.
     */
    @GetMapping("/boards")
    public List<Board> getAllBoards() {
        log.debug("REST request to get all Boards");
        return boardService.findAll();
    }

    /**
     * {@code GET  /boards/:id} : get the "id" board.
     *
     * @param id the id of the board to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the board, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/boards/{id}")
    public ResponseEntity<Board> getBoard(@PathVariable Long id) {
        log.debug("REST request to get Board : {}", id);
        Optional<Board> board = boardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(board);
    }

    /**
     * {@code DELETE  /boards/:id} : delete the "id" board.
     *
     * @param id the id of the board to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        log.debug("REST request to delete Board : {}", id);
        boardService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
