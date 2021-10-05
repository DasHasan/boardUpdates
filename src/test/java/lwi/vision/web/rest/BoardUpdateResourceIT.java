package lwi.vision.web.rest;

import static lwi.vision.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import lwi.vision.IntegrationTest;
import lwi.vision.domain.BoardUpdateEntity;
import lwi.vision.domain.enumeration.UpdateType;
import lwi.vision.repository.BoardUpdateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BoardUpdateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BoardUpdateResourceIT {

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final UpdateType DEFAULT_TYPE = UpdateType.FIRMWARE;
    private static final UpdateType UPDATED_TYPE = UpdateType.SOFTWARE;

    private static final ZonedDateTime DEFAULT_RELEASE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RELEASE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/board-updates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BoardUpdateRepository boardUpdateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBoardUpdateMockMvc;

    private BoardUpdateEntity boardUpdateEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoardUpdateEntity createEntity(EntityManager em) {
        BoardUpdateEntity boardUpdateEntity = new BoardUpdateEntity()
            .version(DEFAULT_VERSION)
            .path(DEFAULT_PATH)
            .type(DEFAULT_TYPE)
            .releaseDate(DEFAULT_RELEASE_DATE);
        return boardUpdateEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoardUpdateEntity createUpdatedEntity(EntityManager em) {
        BoardUpdateEntity boardUpdateEntity = new BoardUpdateEntity()
            .version(UPDATED_VERSION)
            .path(UPDATED_PATH)
            .type(UPDATED_TYPE)
            .releaseDate(UPDATED_RELEASE_DATE);
        return boardUpdateEntity;
    }

    @BeforeEach
    public void initTest() {
        boardUpdateEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createBoardUpdate() throws Exception {
        int databaseSizeBeforeCreate = boardUpdateRepository.findAll().size();
        // Create the BoardUpdate
        restBoardUpdateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boardUpdateEntity))
            )
            .andExpect(status().isCreated());

        // Validate the BoardUpdate in the database
        List<BoardUpdateEntity> boardUpdateList = boardUpdateRepository.findAll();
        assertThat(boardUpdateList).hasSize(databaseSizeBeforeCreate + 1);
        BoardUpdateEntity testBoardUpdate = boardUpdateList.get(boardUpdateList.size() - 1);
        assertThat(testBoardUpdate.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testBoardUpdate.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testBoardUpdate.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBoardUpdate.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
    }

    @Test
    @Transactional
    void createBoardUpdateWithExistingId() throws Exception {
        // Create the BoardUpdate with an existing ID
        boardUpdateEntity.setId(1L);

        int databaseSizeBeforeCreate = boardUpdateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoardUpdateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boardUpdateEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardUpdate in the database
        List<BoardUpdateEntity> boardUpdateList = boardUpdateRepository.findAll();
        assertThat(boardUpdateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBoardUpdates() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList
        restBoardUpdateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boardUpdateEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(sameInstant(DEFAULT_RELEASE_DATE))));
    }

    @Test
    @Transactional
    void getBoardUpdate() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get the boardUpdate
        restBoardUpdateMockMvc
            .perform(get(ENTITY_API_URL_ID, boardUpdateEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(boardUpdateEntity.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.releaseDate").value(sameInstant(DEFAULT_RELEASE_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingBoardUpdate() throws Exception {
        // Get the boardUpdate
        restBoardUpdateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBoardUpdate() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        int databaseSizeBeforeUpdate = boardUpdateRepository.findAll().size();

        // Update the boardUpdate
        BoardUpdateEntity updatedBoardUpdateEntity = boardUpdateRepository.findById(boardUpdateEntity.getId()).get();
        // Disconnect from session so that the updates on updatedBoardUpdateEntity are not directly saved in db
        em.detach(updatedBoardUpdateEntity);
        updatedBoardUpdateEntity.version(UPDATED_VERSION).path(UPDATED_PATH).type(UPDATED_TYPE).releaseDate(UPDATED_RELEASE_DATE);

        restBoardUpdateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBoardUpdateEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBoardUpdateEntity))
            )
            .andExpect(status().isOk());

        // Validate the BoardUpdate in the database
        List<BoardUpdateEntity> boardUpdateList = boardUpdateRepository.findAll();
        assertThat(boardUpdateList).hasSize(databaseSizeBeforeUpdate);
        BoardUpdateEntity testBoardUpdate = boardUpdateList.get(boardUpdateList.size() - 1);
        assertThat(testBoardUpdate.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testBoardUpdate.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testBoardUpdate.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBoardUpdate.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingBoardUpdate() throws Exception {
        int databaseSizeBeforeUpdate = boardUpdateRepository.findAll().size();
        boardUpdateEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoardUpdateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, boardUpdateEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardUpdateEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardUpdate in the database
        List<BoardUpdateEntity> boardUpdateList = boardUpdateRepository.findAll();
        assertThat(boardUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBoardUpdate() throws Exception {
        int databaseSizeBeforeUpdate = boardUpdateRepository.findAll().size();
        boardUpdateEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardUpdateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardUpdateEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardUpdate in the database
        List<BoardUpdateEntity> boardUpdateList = boardUpdateRepository.findAll();
        assertThat(boardUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBoardUpdate() throws Exception {
        int databaseSizeBeforeUpdate = boardUpdateRepository.findAll().size();
        boardUpdateEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardUpdateMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boardUpdateEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BoardUpdate in the database
        List<BoardUpdateEntity> boardUpdateList = boardUpdateRepository.findAll();
        assertThat(boardUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBoardUpdateWithPatch() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        int databaseSizeBeforeUpdate = boardUpdateRepository.findAll().size();

        // Update the boardUpdate using partial update
        BoardUpdateEntity partialUpdatedBoardUpdateEntity = new BoardUpdateEntity();
        partialUpdatedBoardUpdateEntity.setId(boardUpdateEntity.getId());

        partialUpdatedBoardUpdateEntity.path(UPDATED_PATH).type(UPDATED_TYPE);

        restBoardUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoardUpdateEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoardUpdateEntity))
            )
            .andExpect(status().isOk());

        // Validate the BoardUpdate in the database
        List<BoardUpdateEntity> boardUpdateList = boardUpdateRepository.findAll();
        assertThat(boardUpdateList).hasSize(databaseSizeBeforeUpdate);
        BoardUpdateEntity testBoardUpdate = boardUpdateList.get(boardUpdateList.size() - 1);
        assertThat(testBoardUpdate.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testBoardUpdate.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testBoardUpdate.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBoardUpdate.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateBoardUpdateWithPatch() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        int databaseSizeBeforeUpdate = boardUpdateRepository.findAll().size();

        // Update the boardUpdate using partial update
        BoardUpdateEntity partialUpdatedBoardUpdateEntity = new BoardUpdateEntity();
        partialUpdatedBoardUpdateEntity.setId(boardUpdateEntity.getId());

        partialUpdatedBoardUpdateEntity.version(UPDATED_VERSION).path(UPDATED_PATH).type(UPDATED_TYPE).releaseDate(UPDATED_RELEASE_DATE);

        restBoardUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoardUpdateEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoardUpdateEntity))
            )
            .andExpect(status().isOk());

        // Validate the BoardUpdate in the database
        List<BoardUpdateEntity> boardUpdateList = boardUpdateRepository.findAll();
        assertThat(boardUpdateList).hasSize(databaseSizeBeforeUpdate);
        BoardUpdateEntity testBoardUpdate = boardUpdateList.get(boardUpdateList.size() - 1);
        assertThat(testBoardUpdate.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testBoardUpdate.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testBoardUpdate.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBoardUpdate.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingBoardUpdate() throws Exception {
        int databaseSizeBeforeUpdate = boardUpdateRepository.findAll().size();
        boardUpdateEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoardUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, boardUpdateEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boardUpdateEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardUpdate in the database
        List<BoardUpdateEntity> boardUpdateList = boardUpdateRepository.findAll();
        assertThat(boardUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBoardUpdate() throws Exception {
        int databaseSizeBeforeUpdate = boardUpdateRepository.findAll().size();
        boardUpdateEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boardUpdateEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardUpdate in the database
        List<BoardUpdateEntity> boardUpdateList = boardUpdateRepository.findAll();
        assertThat(boardUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBoardUpdate() throws Exception {
        int databaseSizeBeforeUpdate = boardUpdateRepository.findAll().size();
        boardUpdateEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boardUpdateEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BoardUpdate in the database
        List<BoardUpdateEntity> boardUpdateList = boardUpdateRepository.findAll();
        assertThat(boardUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBoardUpdate() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        int databaseSizeBeforeDelete = boardUpdateRepository.findAll().size();

        // Delete the boardUpdate
        restBoardUpdateMockMvc
            .perform(delete(ENTITY_API_URL_ID, boardUpdateEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BoardUpdateEntity> boardUpdateList = boardUpdateRepository.findAll();
        assertThat(boardUpdateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
