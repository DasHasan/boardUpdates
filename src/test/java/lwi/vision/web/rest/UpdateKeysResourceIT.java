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
import lwi.vision.domain.BoardUpdateEntity;
import lwi.vision.domain.UpdateKeysEntity;
import lwi.vision.repository.UpdateKeysRepository;
import lwi.vision.service.criteria.UpdateKeysCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UpdateKeysResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UpdateKeysResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/update-keys";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UpdateKeysRepository updateKeysRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUpdateKeysMockMvc;

    private UpdateKeysEntity updateKeysEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UpdateKeysEntity createEntity(EntityManager em) {
        UpdateKeysEntity updateKeysEntity = new UpdateKeysEntity().key(DEFAULT_KEY);
        return updateKeysEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UpdateKeysEntity createUpdatedEntity(EntityManager em) {
        UpdateKeysEntity updateKeysEntity = new UpdateKeysEntity().key(UPDATED_KEY);
        return updateKeysEntity;
    }

    @BeforeEach
    public void initTest() {
        updateKeysEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createUpdateKeys() throws Exception {
        int databaseSizeBeforeCreate = updateKeysRepository.findAll().size();
        // Create the UpdateKeys
        restUpdateKeysMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updateKeysEntity))
            )
            .andExpect(status().isCreated());

        // Validate the UpdateKeys in the database
        List<UpdateKeysEntity> updateKeysList = updateKeysRepository.findAll();
        assertThat(updateKeysList).hasSize(databaseSizeBeforeCreate + 1);
        UpdateKeysEntity testUpdateKeys = updateKeysList.get(updateKeysList.size() - 1);
        assertThat(testUpdateKeys.getKey()).isEqualTo(DEFAULT_KEY);
    }

    @Test
    @Transactional
    void createUpdateKeysWithExistingId() throws Exception {
        // Create the UpdateKeys with an existing ID
        updateKeysEntity.setId(1L);

        int databaseSizeBeforeCreate = updateKeysRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUpdateKeysMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updateKeysEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the UpdateKeys in the database
        List<UpdateKeysEntity> updateKeysList = updateKeysRepository.findAll();
        assertThat(updateKeysList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUpdateKeys() throws Exception {
        // Initialize the database
        updateKeysRepository.saveAndFlush(updateKeysEntity);

        // Get all the updateKeysList
        restUpdateKeysMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(updateKeysEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)));
    }

    @Test
    @Transactional
    void getUpdateKeys() throws Exception {
        // Initialize the database
        updateKeysRepository.saveAndFlush(updateKeysEntity);

        // Get the updateKeys
        restUpdateKeysMockMvc
            .perform(get(ENTITY_API_URL_ID, updateKeysEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(updateKeysEntity.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY));
    }

    @Test
    @Transactional
    void getUpdateKeysByIdFiltering() throws Exception {
        // Initialize the database
        updateKeysRepository.saveAndFlush(updateKeysEntity);

        Long id = updateKeysEntity.getId();

        defaultUpdateKeysShouldBeFound("id.equals=" + id);
        defaultUpdateKeysShouldNotBeFound("id.notEquals=" + id);

        defaultUpdateKeysShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUpdateKeysShouldNotBeFound("id.greaterThan=" + id);

        defaultUpdateKeysShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUpdateKeysShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUpdateKeysByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        updateKeysRepository.saveAndFlush(updateKeysEntity);

        // Get all the updateKeysList where key equals to DEFAULT_KEY
        defaultUpdateKeysShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the updateKeysList where key equals to UPDATED_KEY
        defaultUpdateKeysShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllUpdateKeysByKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        updateKeysRepository.saveAndFlush(updateKeysEntity);

        // Get all the updateKeysList where key not equals to DEFAULT_KEY
        defaultUpdateKeysShouldNotBeFound("key.notEquals=" + DEFAULT_KEY);

        // Get all the updateKeysList where key not equals to UPDATED_KEY
        defaultUpdateKeysShouldBeFound("key.notEquals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllUpdateKeysByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        updateKeysRepository.saveAndFlush(updateKeysEntity);

        // Get all the updateKeysList where key in DEFAULT_KEY or UPDATED_KEY
        defaultUpdateKeysShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the updateKeysList where key equals to UPDATED_KEY
        defaultUpdateKeysShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllUpdateKeysByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        updateKeysRepository.saveAndFlush(updateKeysEntity);

        // Get all the updateKeysList where key is not null
        defaultUpdateKeysShouldBeFound("key.specified=true");

        // Get all the updateKeysList where key is null
        defaultUpdateKeysShouldNotBeFound("key.specified=false");
    }

    @Test
    @Transactional
    void getAllUpdateKeysByKeyContainsSomething() throws Exception {
        // Initialize the database
        updateKeysRepository.saveAndFlush(updateKeysEntity);

        // Get all the updateKeysList where key contains DEFAULT_KEY
        defaultUpdateKeysShouldBeFound("key.contains=" + DEFAULT_KEY);

        // Get all the updateKeysList where key contains UPDATED_KEY
        defaultUpdateKeysShouldNotBeFound("key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllUpdateKeysByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        updateKeysRepository.saveAndFlush(updateKeysEntity);

        // Get all the updateKeysList where key does not contain DEFAULT_KEY
        defaultUpdateKeysShouldNotBeFound("key.doesNotContain=" + DEFAULT_KEY);

        // Get all the updateKeysList where key does not contain UPDATED_KEY
        defaultUpdateKeysShouldBeFound("key.doesNotContain=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllUpdateKeysByBoardUpdateIsEqualToSomething() throws Exception {
        // Initialize the database
        updateKeysRepository.saveAndFlush(updateKeysEntity);
        BoardUpdateEntity boardUpdate = BoardUpdateResourceIT.createEntity(em);
        em.persist(boardUpdate);
        em.flush();
        updateKeysEntity.setBoardUpdate(boardUpdate);
        updateKeysRepository.saveAndFlush(updateKeysEntity);
        Long boardUpdateId = boardUpdate.getId();

        // Get all the updateKeysList where boardUpdate equals to boardUpdateId
        defaultUpdateKeysShouldBeFound("boardUpdateId.equals=" + boardUpdateId);

        // Get all the updateKeysList where boardUpdate equals to (boardUpdateId + 1)
        defaultUpdateKeysShouldNotBeFound("boardUpdateId.equals=" + (boardUpdateId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUpdateKeysShouldBeFound(String filter) throws Exception {
        restUpdateKeysMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(updateKeysEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)));

        // Check, that the count call also returns 1
        restUpdateKeysMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUpdateKeysShouldNotBeFound(String filter) throws Exception {
        restUpdateKeysMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUpdateKeysMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUpdateKeys() throws Exception {
        // Get the updateKeys
        restUpdateKeysMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUpdateKeys() throws Exception {
        // Initialize the database
        updateKeysRepository.saveAndFlush(updateKeysEntity);

        int databaseSizeBeforeUpdate = updateKeysRepository.findAll().size();

        // Update the updateKeys
        UpdateKeysEntity updatedUpdateKeysEntity = updateKeysRepository.findById(updateKeysEntity.getId()).get();
        // Disconnect from session so that the updates on updatedUpdateKeysEntity are not directly saved in db
        em.detach(updatedUpdateKeysEntity);
        updatedUpdateKeysEntity.key(UPDATED_KEY);

        restUpdateKeysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUpdateKeysEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUpdateKeysEntity))
            )
            .andExpect(status().isOk());

        // Validate the UpdateKeys in the database
        List<UpdateKeysEntity> updateKeysList = updateKeysRepository.findAll();
        assertThat(updateKeysList).hasSize(databaseSizeBeforeUpdate);
        UpdateKeysEntity testUpdateKeys = updateKeysList.get(updateKeysList.size() - 1);
        assertThat(testUpdateKeys.getKey()).isEqualTo(UPDATED_KEY);
    }

    @Test
    @Transactional
    void putNonExistingUpdateKeys() throws Exception {
        int databaseSizeBeforeUpdate = updateKeysRepository.findAll().size();
        updateKeysEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUpdateKeysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updateKeysEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updateKeysEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the UpdateKeys in the database
        List<UpdateKeysEntity> updateKeysList = updateKeysRepository.findAll();
        assertThat(updateKeysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUpdateKeys() throws Exception {
        int databaseSizeBeforeUpdate = updateKeysRepository.findAll().size();
        updateKeysEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpdateKeysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updateKeysEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the UpdateKeys in the database
        List<UpdateKeysEntity> updateKeysList = updateKeysRepository.findAll();
        assertThat(updateKeysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUpdateKeys() throws Exception {
        int databaseSizeBeforeUpdate = updateKeysRepository.findAll().size();
        updateKeysEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpdateKeysMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updateKeysEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UpdateKeys in the database
        List<UpdateKeysEntity> updateKeysList = updateKeysRepository.findAll();
        assertThat(updateKeysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUpdateKeysWithPatch() throws Exception {
        // Initialize the database
        updateKeysRepository.saveAndFlush(updateKeysEntity);

        int databaseSizeBeforeUpdate = updateKeysRepository.findAll().size();

        // Update the updateKeys using partial update
        UpdateKeysEntity partialUpdatedUpdateKeysEntity = new UpdateKeysEntity();
        partialUpdatedUpdateKeysEntity.setId(updateKeysEntity.getId());

        partialUpdatedUpdateKeysEntity.key(UPDATED_KEY);

        restUpdateKeysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUpdateKeysEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUpdateKeysEntity))
            )
            .andExpect(status().isOk());

        // Validate the UpdateKeys in the database
        List<UpdateKeysEntity> updateKeysList = updateKeysRepository.findAll();
        assertThat(updateKeysList).hasSize(databaseSizeBeforeUpdate);
        UpdateKeysEntity testUpdateKeys = updateKeysList.get(updateKeysList.size() - 1);
        assertThat(testUpdateKeys.getKey()).isEqualTo(UPDATED_KEY);
    }

    @Test
    @Transactional
    void fullUpdateUpdateKeysWithPatch() throws Exception {
        // Initialize the database
        updateKeysRepository.saveAndFlush(updateKeysEntity);

        int databaseSizeBeforeUpdate = updateKeysRepository.findAll().size();

        // Update the updateKeys using partial update
        UpdateKeysEntity partialUpdatedUpdateKeysEntity = new UpdateKeysEntity();
        partialUpdatedUpdateKeysEntity.setId(updateKeysEntity.getId());

        partialUpdatedUpdateKeysEntity.key(UPDATED_KEY);

        restUpdateKeysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUpdateKeysEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUpdateKeysEntity))
            )
            .andExpect(status().isOk());

        // Validate the UpdateKeys in the database
        List<UpdateKeysEntity> updateKeysList = updateKeysRepository.findAll();
        assertThat(updateKeysList).hasSize(databaseSizeBeforeUpdate);
        UpdateKeysEntity testUpdateKeys = updateKeysList.get(updateKeysList.size() - 1);
        assertThat(testUpdateKeys.getKey()).isEqualTo(UPDATED_KEY);
    }

    @Test
    @Transactional
    void patchNonExistingUpdateKeys() throws Exception {
        int databaseSizeBeforeUpdate = updateKeysRepository.findAll().size();
        updateKeysEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUpdateKeysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, updateKeysEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(updateKeysEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the UpdateKeys in the database
        List<UpdateKeysEntity> updateKeysList = updateKeysRepository.findAll();
        assertThat(updateKeysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUpdateKeys() throws Exception {
        int databaseSizeBeforeUpdate = updateKeysRepository.findAll().size();
        updateKeysEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpdateKeysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(updateKeysEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the UpdateKeys in the database
        List<UpdateKeysEntity> updateKeysList = updateKeysRepository.findAll();
        assertThat(updateKeysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUpdateKeys() throws Exception {
        int databaseSizeBeforeUpdate = updateKeysRepository.findAll().size();
        updateKeysEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpdateKeysMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(updateKeysEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UpdateKeys in the database
        List<UpdateKeysEntity> updateKeysList = updateKeysRepository.findAll();
        assertThat(updateKeysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUpdateKeys() throws Exception {
        // Initialize the database
        updateKeysRepository.saveAndFlush(updateKeysEntity);

        int databaseSizeBeforeDelete = updateKeysRepository.findAll().size();

        // Delete the updateKeys
        restUpdateKeysMockMvc
            .perform(delete(ENTITY_API_URL_ID, updateKeysEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UpdateKeysEntity> updateKeysList = updateKeysRepository.findAll();
        assertThat(updateKeysList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
