package lwi.vision.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import lwi.vision.IntegrationTest;
import lwi.vision.domain.Board;
import lwi.vision.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BoardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BoardResourceIT {

    private static final String DEFAULT_SERIAL = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/boards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBoardMockMvc;

    private Board board;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Board createEntity(EntityManager em) {
        Board board = new Board().serial(DEFAULT_SERIAL);
        return board;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Board createUpdatedEntity(EntityManager em) {
        Board board = new Board().serial(UPDATED_SERIAL);
        return board;
    }

    @BeforeEach
    public void initTest() {
        board = createEntity(em);
    }

    @Test
    @Transactional
    void createBoard() throws Exception {
        int databaseSizeBeforeCreate = boardRepository.findAll().size();
        // Create the Board
        restBoardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(board)))
            .andExpect(status().isCreated());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeCreate + 1);
        Board testBoard = boardList.get(boardList.size() - 1);
        assertThat(testBoard.getSerial()).isEqualTo(DEFAULT_SERIAL);
    }

    @Test
    @Transactional
    void createBoardWithExistingId() throws Exception {
        // Create the Board with an existing ID
        board.setId(1L);

        int databaseSizeBeforeCreate = boardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(board)))
            .andExpect(status().isBadRequest());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBoards() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get all the boardList
        restBoardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(board.getId().intValue())))
            .andExpect(jsonPath("$.[*].serial").value(hasItem(DEFAULT_SERIAL)));
    }

    @Test
    @Transactional
    void getBoard() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        // Get the board
        restBoardMockMvc
            .perform(get(ENTITY_API_URL_ID, board.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(board.getId().intValue()))
            .andExpect(jsonPath("$.serial").value(DEFAULT_SERIAL));
    }

    @Test
    @Transactional
    void getNonExistingBoard() throws Exception {
        // Get the board
        restBoardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBoard() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        int databaseSizeBeforeUpdate = boardRepository.findAll().size();

        // Update the board
        Board updatedBoard = boardRepository.findById(board.getId()).get();
        // Disconnect from session so that the updates on updatedBoard are not directly saved in db
        em.detach(updatedBoard);
        updatedBoard.serial(UPDATED_SERIAL);

        restBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBoard.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBoard))
            )
            .andExpect(status().isOk());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
        Board testBoard = boardList.get(boardList.size() - 1);
        assertThat(testBoard.getSerial()).isEqualTo(UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void putNonExistingBoard() throws Exception {
        int databaseSizeBeforeUpdate = boardRepository.findAll().size();
        board.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, board.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(board))
            )
            .andExpect(status().isBadRequest());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBoard() throws Exception {
        int databaseSizeBeforeUpdate = boardRepository.findAll().size();
        board.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(board))
            )
            .andExpect(status().isBadRequest());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBoard() throws Exception {
        int databaseSizeBeforeUpdate = boardRepository.findAll().size();
        board.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(board)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBoardWithPatch() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        int databaseSizeBeforeUpdate = boardRepository.findAll().size();

        // Update the board using partial update
        Board partialUpdatedBoard = new Board();
        partialUpdatedBoard.setId(board.getId());

        partialUpdatedBoard.serial(UPDATED_SERIAL);

        restBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoard))
            )
            .andExpect(status().isOk());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
        Board testBoard = boardList.get(boardList.size() - 1);
        assertThat(testBoard.getSerial()).isEqualTo(UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void fullUpdateBoardWithPatch() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        int databaseSizeBeforeUpdate = boardRepository.findAll().size();

        // Update the board using partial update
        Board partialUpdatedBoard = new Board();
        partialUpdatedBoard.setId(board.getId());

        partialUpdatedBoard.serial(UPDATED_SERIAL);

        restBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoard))
            )
            .andExpect(status().isOk());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
        Board testBoard = boardList.get(boardList.size() - 1);
        assertThat(testBoard.getSerial()).isEqualTo(UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void patchNonExistingBoard() throws Exception {
        int databaseSizeBeforeUpdate = boardRepository.findAll().size();
        board.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, board.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(board))
            )
            .andExpect(status().isBadRequest());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBoard() throws Exception {
        int databaseSizeBeforeUpdate = boardRepository.findAll().size();
        board.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(board))
            )
            .andExpect(status().isBadRequest());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBoard() throws Exception {
        int databaseSizeBeforeUpdate = boardRepository.findAll().size();
        board.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(board)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Board in the database
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBoard() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(board);

        int databaseSizeBeforeDelete = boardRepository.findAll().size();

        // Delete the board
        restBoardMockMvc
            .perform(delete(ENTITY_API_URL_ID, board.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
