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
import lwi.vision.domain.BoardEntity;
import lwi.vision.domain.BoardUpdateEntity;
import lwi.vision.domain.enumeration.UpdateType;
import lwi.vision.repository.BoardUpdateRepository;
import lwi.vision.service.criteria.BoardUpdateCriteria;
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
    private static final ZonedDateTime SMALLER_RELEASE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

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
    void getBoardUpdatesByIdFiltering() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        Long id = boardUpdateEntity.getId();

        defaultBoardUpdateShouldBeFound("id.equals=" + id);
        defaultBoardUpdateShouldNotBeFound("id.notEquals=" + id);

        defaultBoardUpdateShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBoardUpdateShouldNotBeFound("id.greaterThan=" + id);

        defaultBoardUpdateShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBoardUpdateShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where version equals to DEFAULT_VERSION
        defaultBoardUpdateShouldBeFound("version.equals=" + DEFAULT_VERSION);

        // Get all the boardUpdateList where version equals to UPDATED_VERSION
        defaultBoardUpdateShouldNotBeFound("version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByVersionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where version not equals to DEFAULT_VERSION
        defaultBoardUpdateShouldNotBeFound("version.notEquals=" + DEFAULT_VERSION);

        // Get all the boardUpdateList where version not equals to UPDATED_VERSION
        defaultBoardUpdateShouldBeFound("version.notEquals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where version in DEFAULT_VERSION or UPDATED_VERSION
        defaultBoardUpdateShouldBeFound("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION);

        // Get all the boardUpdateList where version equals to UPDATED_VERSION
        defaultBoardUpdateShouldNotBeFound("version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where version is not null
        defaultBoardUpdateShouldBeFound("version.specified=true");

        // Get all the boardUpdateList where version is null
        defaultBoardUpdateShouldNotBeFound("version.specified=false");
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByVersionContainsSomething() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where version contains DEFAULT_VERSION
        defaultBoardUpdateShouldBeFound("version.contains=" + DEFAULT_VERSION);

        // Get all the boardUpdateList where version contains UPDATED_VERSION
        defaultBoardUpdateShouldNotBeFound("version.contains=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByVersionNotContainsSomething() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where version does not contain DEFAULT_VERSION
        defaultBoardUpdateShouldNotBeFound("version.doesNotContain=" + DEFAULT_VERSION);

        // Get all the boardUpdateList where version does not contain UPDATED_VERSION
        defaultBoardUpdateShouldBeFound("version.doesNotContain=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where path equals to DEFAULT_PATH
        defaultBoardUpdateShouldBeFound("path.equals=" + DEFAULT_PATH);

        // Get all the boardUpdateList where path equals to UPDATED_PATH
        defaultBoardUpdateShouldNotBeFound("path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByPathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where path not equals to DEFAULT_PATH
        defaultBoardUpdateShouldNotBeFound("path.notEquals=" + DEFAULT_PATH);

        // Get all the boardUpdateList where path not equals to UPDATED_PATH
        defaultBoardUpdateShouldBeFound("path.notEquals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByPathIsInShouldWork() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where path in DEFAULT_PATH or UPDATED_PATH
        defaultBoardUpdateShouldBeFound("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH);

        // Get all the boardUpdateList where path equals to UPDATED_PATH
        defaultBoardUpdateShouldNotBeFound("path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where path is not null
        defaultBoardUpdateShouldBeFound("path.specified=true");

        // Get all the boardUpdateList where path is null
        defaultBoardUpdateShouldNotBeFound("path.specified=false");
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByPathContainsSomething() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where path contains DEFAULT_PATH
        defaultBoardUpdateShouldBeFound("path.contains=" + DEFAULT_PATH);

        // Get all the boardUpdateList where path contains UPDATED_PATH
        defaultBoardUpdateShouldNotBeFound("path.contains=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByPathNotContainsSomething() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where path does not contain DEFAULT_PATH
        defaultBoardUpdateShouldNotBeFound("path.doesNotContain=" + DEFAULT_PATH);

        // Get all the boardUpdateList where path does not contain UPDATED_PATH
        defaultBoardUpdateShouldBeFound("path.doesNotContain=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where type equals to DEFAULT_TYPE
        defaultBoardUpdateShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the boardUpdateList where type equals to UPDATED_TYPE
        defaultBoardUpdateShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where type not equals to DEFAULT_TYPE
        defaultBoardUpdateShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the boardUpdateList where type not equals to UPDATED_TYPE
        defaultBoardUpdateShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultBoardUpdateShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the boardUpdateList where type equals to UPDATED_TYPE
        defaultBoardUpdateShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where type is not null
        defaultBoardUpdateShouldBeFound("type.specified=true");

        // Get all the boardUpdateList where type is null
        defaultBoardUpdateShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByReleaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where releaseDate equals to DEFAULT_RELEASE_DATE
        defaultBoardUpdateShouldBeFound("releaseDate.equals=" + DEFAULT_RELEASE_DATE);

        // Get all the boardUpdateList where releaseDate equals to UPDATED_RELEASE_DATE
        defaultBoardUpdateShouldNotBeFound("releaseDate.equals=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByReleaseDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where releaseDate not equals to DEFAULT_RELEASE_DATE
        defaultBoardUpdateShouldNotBeFound("releaseDate.notEquals=" + DEFAULT_RELEASE_DATE);

        // Get all the boardUpdateList where releaseDate not equals to UPDATED_RELEASE_DATE
        defaultBoardUpdateShouldBeFound("releaseDate.notEquals=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByReleaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where releaseDate in DEFAULT_RELEASE_DATE or UPDATED_RELEASE_DATE
        defaultBoardUpdateShouldBeFound("releaseDate.in=" + DEFAULT_RELEASE_DATE + "," + UPDATED_RELEASE_DATE);

        // Get all the boardUpdateList where releaseDate equals to UPDATED_RELEASE_DATE
        defaultBoardUpdateShouldNotBeFound("releaseDate.in=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByReleaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where releaseDate is not null
        defaultBoardUpdateShouldBeFound("releaseDate.specified=true");

        // Get all the boardUpdateList where releaseDate is null
        defaultBoardUpdateShouldNotBeFound("releaseDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByReleaseDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where releaseDate is greater than or equal to DEFAULT_RELEASE_DATE
        defaultBoardUpdateShouldBeFound("releaseDate.greaterThanOrEqual=" + DEFAULT_RELEASE_DATE);

        // Get all the boardUpdateList where releaseDate is greater than or equal to UPDATED_RELEASE_DATE
        defaultBoardUpdateShouldNotBeFound("releaseDate.greaterThanOrEqual=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByReleaseDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where releaseDate is less than or equal to DEFAULT_RELEASE_DATE
        defaultBoardUpdateShouldBeFound("releaseDate.lessThanOrEqual=" + DEFAULT_RELEASE_DATE);

        // Get all the boardUpdateList where releaseDate is less than or equal to SMALLER_RELEASE_DATE
        defaultBoardUpdateShouldNotBeFound("releaseDate.lessThanOrEqual=" + SMALLER_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByReleaseDateIsLessThanSomething() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where releaseDate is less than DEFAULT_RELEASE_DATE
        defaultBoardUpdateShouldNotBeFound("releaseDate.lessThan=" + DEFAULT_RELEASE_DATE);

        // Get all the boardUpdateList where releaseDate is less than UPDATED_RELEASE_DATE
        defaultBoardUpdateShouldBeFound("releaseDate.lessThan=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByReleaseDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);

        // Get all the boardUpdateList where releaseDate is greater than DEFAULT_RELEASE_DATE
        defaultBoardUpdateShouldNotBeFound("releaseDate.greaterThan=" + DEFAULT_RELEASE_DATE);

        // Get all the boardUpdateList where releaseDate is greater than SMALLER_RELEASE_DATE
        defaultBoardUpdateShouldBeFound("releaseDate.greaterThan=" + SMALLER_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllBoardUpdatesByBoardIsEqualToSomething() throws Exception {
        // Initialize the database
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);
        BoardEntity board = BoardResourceIT.createEntity(em);
        em.persist(board);
        em.flush();
        boardUpdateEntity.setBoard(board);
        boardUpdateRepository.saveAndFlush(boardUpdateEntity);
        Long boardId = board.getId();

        // Get all the boardUpdateList where board equals to boardId
        defaultBoardUpdateShouldBeFound("boardId.equals=" + boardId);

        // Get all the boardUpdateList where board equals to (boardId + 1)
        defaultBoardUpdateShouldNotBeFound("boardId.equals=" + (boardId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBoardUpdateShouldBeFound(String filter) throws Exception {
        restBoardUpdateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boardUpdateEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(sameInstant(DEFAULT_RELEASE_DATE))));

        // Check, that the count call also returns 1
        restBoardUpdateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBoardUpdateShouldNotBeFound(String filter) throws Exception {
        restBoardUpdateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBoardUpdateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
