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
import lwi.vision.domain.BoardEntity;
import lwi.vision.domain.FirmwareEntity;
import lwi.vision.domain.SoftwareEntity;
import lwi.vision.repository.BoardRepository;
import lwi.vision.service.criteria.BoardCriteria;
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

    private BoardEntity boardEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoardEntity createEntity(EntityManager em) {
        BoardEntity boardEntity = new BoardEntity().serial(DEFAULT_SERIAL);
        return boardEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoardEntity createUpdatedEntity(EntityManager em) {
        BoardEntity boardEntity = new BoardEntity().serial(UPDATED_SERIAL);
        return boardEntity;
    }

    @BeforeEach
    public void initTest() {
        boardEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createBoard() throws Exception {
        int databaseSizeBeforeCreate = boardRepository.findAll().size();
        // Create the Board
        restBoardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boardEntity)))
            .andExpect(status().isCreated());

        // Validate the Board in the database
        List<BoardEntity> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeCreate + 1);
        BoardEntity testBoard = boardList.get(boardList.size() - 1);
        assertThat(testBoard.getSerial()).isEqualTo(DEFAULT_SERIAL);
    }

    @Test
    @Transactional
    void createBoardWithExistingId() throws Exception {
        // Create the Board with an existing ID
        boardEntity.setId(1L);

        int databaseSizeBeforeCreate = boardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boardEntity)))
            .andExpect(status().isBadRequest());

        // Validate the Board in the database
        List<BoardEntity> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBoards() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(boardEntity);

        // Get all the boardList
        restBoardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boardEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].serial").value(hasItem(DEFAULT_SERIAL)));
    }

    @Test
    @Transactional
    void getBoard() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(boardEntity);

        // Get the board
        restBoardMockMvc
            .perform(get(ENTITY_API_URL_ID, boardEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(boardEntity.getId().intValue()))
            .andExpect(jsonPath("$.serial").value(DEFAULT_SERIAL));
    }

    @Test
    @Transactional
    void getBoardsByIdFiltering() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(boardEntity);

        Long id = boardEntity.getId();

        defaultBoardShouldBeFound("id.equals=" + id);
        defaultBoardShouldNotBeFound("id.notEquals=" + id);

        defaultBoardShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBoardShouldNotBeFound("id.greaterThan=" + id);

        defaultBoardShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBoardShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBoardsBySerialIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(boardEntity);

        // Get all the boardList where serial equals to DEFAULT_SERIAL
        defaultBoardShouldBeFound("serial.equals=" + DEFAULT_SERIAL);

        // Get all the boardList where serial equals to UPDATED_SERIAL
        defaultBoardShouldNotBeFound("serial.equals=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllBoardsBySerialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(boardEntity);

        // Get all the boardList where serial not equals to DEFAULT_SERIAL
        defaultBoardShouldNotBeFound("serial.notEquals=" + DEFAULT_SERIAL);

        // Get all the boardList where serial not equals to UPDATED_SERIAL
        defaultBoardShouldBeFound("serial.notEquals=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllBoardsBySerialIsInShouldWork() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(boardEntity);

        // Get all the boardList where serial in DEFAULT_SERIAL or UPDATED_SERIAL
        defaultBoardShouldBeFound("serial.in=" + DEFAULT_SERIAL + "," + UPDATED_SERIAL);

        // Get all the boardList where serial equals to UPDATED_SERIAL
        defaultBoardShouldNotBeFound("serial.in=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllBoardsBySerialIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(boardEntity);

        // Get all the boardList where serial is not null
        defaultBoardShouldBeFound("serial.specified=true");

        // Get all the boardList where serial is null
        defaultBoardShouldNotBeFound("serial.specified=false");
    }

    @Test
    @Transactional
    void getAllBoardsBySerialContainsSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(boardEntity);

        // Get all the boardList where serial contains DEFAULT_SERIAL
        defaultBoardShouldBeFound("serial.contains=" + DEFAULT_SERIAL);

        // Get all the boardList where serial contains UPDATED_SERIAL
        defaultBoardShouldNotBeFound("serial.contains=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllBoardsBySerialNotContainsSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(boardEntity);

        // Get all the boardList where serial does not contain DEFAULT_SERIAL
        defaultBoardShouldNotBeFound("serial.doesNotContain=" + DEFAULT_SERIAL);

        // Get all the boardList where serial does not contain UPDATED_SERIAL
        defaultBoardShouldBeFound("serial.doesNotContain=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllBoardsByFirmwareIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(boardEntity);
        FirmwareEntity firmware = FirmwareResourceIT.createEntity(em);
        em.persist(firmware);
        em.flush();
        boardEntity.addFirmware(firmware);
        boardRepository.saveAndFlush(boardEntity);
        Long firmwareId = firmware.getId();

        // Get all the boardList where firmware equals to firmwareId
        defaultBoardShouldBeFound("firmwareId.equals=" + firmwareId);

        // Get all the boardList where firmware equals to (firmwareId + 1)
        defaultBoardShouldNotBeFound("firmwareId.equals=" + (firmwareId + 1));
    }

    @Test
    @Transactional
    void getAllBoardsBySoftwareIsEqualToSomething() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(boardEntity);
        SoftwareEntity software = SoftwareResourceIT.createEntity(em);
        em.persist(software);
        em.flush();
        boardEntity.addSoftware(software);
        boardRepository.saveAndFlush(boardEntity);
        Long softwareId = software.getId();

        // Get all the boardList where software equals to softwareId
        defaultBoardShouldBeFound("softwareId.equals=" + softwareId);

        // Get all the boardList where software equals to (softwareId + 1)
        defaultBoardShouldNotBeFound("softwareId.equals=" + (softwareId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBoardShouldBeFound(String filter) throws Exception {
        restBoardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boardEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].serial").value(hasItem(DEFAULT_SERIAL)));

        // Check, that the count call also returns 1
        restBoardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBoardShouldNotBeFound(String filter) throws Exception {
        restBoardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBoardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        boardRepository.saveAndFlush(boardEntity);

        int databaseSizeBeforeUpdate = boardRepository.findAll().size();

        // Update the board
        BoardEntity updatedBoardEntity = boardRepository.findById(boardEntity.getId()).get();
        // Disconnect from session so that the updates on updatedBoardEntity are not directly saved in db
        em.detach(updatedBoardEntity);
        updatedBoardEntity.serial(UPDATED_SERIAL);

        restBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBoardEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBoardEntity))
            )
            .andExpect(status().isOk());

        // Validate the Board in the database
        List<BoardEntity> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
        BoardEntity testBoard = boardList.get(boardList.size() - 1);
        assertThat(testBoard.getSerial()).isEqualTo(UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void putNonExistingBoard() throws Exception {
        int databaseSizeBeforeUpdate = boardRepository.findAll().size();
        boardEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, boardEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Board in the database
        List<BoardEntity> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBoard() throws Exception {
        int databaseSizeBeforeUpdate = boardRepository.findAll().size();
        boardEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Board in the database
        List<BoardEntity> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBoard() throws Exception {
        int databaseSizeBeforeUpdate = boardRepository.findAll().size();
        boardEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boardEntity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Board in the database
        List<BoardEntity> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBoardWithPatch() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(boardEntity);

        int databaseSizeBeforeUpdate = boardRepository.findAll().size();

        // Update the board using partial update
        BoardEntity partialUpdatedBoardEntity = new BoardEntity();
        partialUpdatedBoardEntity.setId(boardEntity.getId());

        partialUpdatedBoardEntity.serial(UPDATED_SERIAL);

        restBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoardEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoardEntity))
            )
            .andExpect(status().isOk());

        // Validate the Board in the database
        List<BoardEntity> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
        BoardEntity testBoard = boardList.get(boardList.size() - 1);
        assertThat(testBoard.getSerial()).isEqualTo(UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void fullUpdateBoardWithPatch() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(boardEntity);

        int databaseSizeBeforeUpdate = boardRepository.findAll().size();

        // Update the board using partial update
        BoardEntity partialUpdatedBoardEntity = new BoardEntity();
        partialUpdatedBoardEntity.setId(boardEntity.getId());

        partialUpdatedBoardEntity.serial(UPDATED_SERIAL);

        restBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoardEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoardEntity))
            )
            .andExpect(status().isOk());

        // Validate the Board in the database
        List<BoardEntity> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
        BoardEntity testBoard = boardList.get(boardList.size() - 1);
        assertThat(testBoard.getSerial()).isEqualTo(UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void patchNonExistingBoard() throws Exception {
        int databaseSizeBeforeUpdate = boardRepository.findAll().size();
        boardEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, boardEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boardEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Board in the database
        List<BoardEntity> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBoard() throws Exception {
        int databaseSizeBeforeUpdate = boardRepository.findAll().size();
        boardEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boardEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Board in the database
        List<BoardEntity> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBoard() throws Exception {
        int databaseSizeBeforeUpdate = boardRepository.findAll().size();
        boardEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(boardEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Board in the database
        List<BoardEntity> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBoard() throws Exception {
        // Initialize the database
        boardRepository.saveAndFlush(boardEntity);

        int databaseSizeBeforeDelete = boardRepository.findAll().size();

        // Delete the board
        restBoardMockMvc
            .perform(delete(ENTITY_API_URL_ID, boardEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BoardEntity> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
