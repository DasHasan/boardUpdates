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
import lwi.vision.domain.FirmwareUpdateEntity;
import lwi.vision.repository.FirmwareUpdateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FirmwareUpdateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FirmwareUpdateResourceIT {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/firmware-updates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FirmwareUpdateRepository firmwareUpdateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFirmwareUpdateMockMvc;

    private FirmwareUpdateEntity firmwareUpdateEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FirmwareUpdateEntity createEntity(EntityManager em) {
        FirmwareUpdateEntity firmwareUpdateEntity = new FirmwareUpdateEntity().active(DEFAULT_ACTIVE);
        return firmwareUpdateEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FirmwareUpdateEntity createUpdatedEntity(EntityManager em) {
        FirmwareUpdateEntity firmwareUpdateEntity = new FirmwareUpdateEntity().active(UPDATED_ACTIVE);
        return firmwareUpdateEntity;
    }

    @BeforeEach
    public void initTest() {
        firmwareUpdateEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createFirmwareUpdate() throws Exception {
        int databaseSizeBeforeCreate = firmwareUpdateRepository.findAll().size();
        // Create the FirmwareUpdate
        restFirmwareUpdateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(firmwareUpdateEntity))
            )
            .andExpect(status().isCreated());

        // Validate the FirmwareUpdate in the database
        List<FirmwareUpdateEntity> firmwareUpdateList = firmwareUpdateRepository.findAll();
        assertThat(firmwareUpdateList).hasSize(databaseSizeBeforeCreate + 1);
        FirmwareUpdateEntity testFirmwareUpdate = firmwareUpdateList.get(firmwareUpdateList.size() - 1);
        assertThat(testFirmwareUpdate.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createFirmwareUpdateWithExistingId() throws Exception {
        // Create the FirmwareUpdate with an existing ID
        firmwareUpdateEntity.setId(1L);

        int databaseSizeBeforeCreate = firmwareUpdateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFirmwareUpdateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(firmwareUpdateEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the FirmwareUpdate in the database
        List<FirmwareUpdateEntity> firmwareUpdateList = firmwareUpdateRepository.findAll();
        assertThat(firmwareUpdateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFirmwareUpdates() throws Exception {
        // Initialize the database
        firmwareUpdateRepository.saveAndFlush(firmwareUpdateEntity);

        // Get all the firmwareUpdateList
        restFirmwareUpdateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(firmwareUpdateEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getFirmwareUpdate() throws Exception {
        // Initialize the database
        firmwareUpdateRepository.saveAndFlush(firmwareUpdateEntity);

        // Get the firmwareUpdate
        restFirmwareUpdateMockMvc
            .perform(get(ENTITY_API_URL_ID, firmwareUpdateEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(firmwareUpdateEntity.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFirmwareUpdate() throws Exception {
        // Get the firmwareUpdate
        restFirmwareUpdateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFirmwareUpdate() throws Exception {
        // Initialize the database
        firmwareUpdateRepository.saveAndFlush(firmwareUpdateEntity);

        int databaseSizeBeforeUpdate = firmwareUpdateRepository.findAll().size();

        // Update the firmwareUpdate
        FirmwareUpdateEntity updatedFirmwareUpdateEntity = firmwareUpdateRepository.findById(firmwareUpdateEntity.getId()).get();
        // Disconnect from session so that the updates on updatedFirmwareUpdateEntity are not directly saved in db
        em.detach(updatedFirmwareUpdateEntity);
        updatedFirmwareUpdateEntity.active(UPDATED_ACTIVE);

        restFirmwareUpdateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFirmwareUpdateEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFirmwareUpdateEntity))
            )
            .andExpect(status().isOk());

        // Validate the FirmwareUpdate in the database
        List<FirmwareUpdateEntity> firmwareUpdateList = firmwareUpdateRepository.findAll();
        assertThat(firmwareUpdateList).hasSize(databaseSizeBeforeUpdate);
        FirmwareUpdateEntity testFirmwareUpdate = firmwareUpdateList.get(firmwareUpdateList.size() - 1);
        assertThat(testFirmwareUpdate.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingFirmwareUpdate() throws Exception {
        int databaseSizeBeforeUpdate = firmwareUpdateRepository.findAll().size();
        firmwareUpdateEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFirmwareUpdateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, firmwareUpdateEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(firmwareUpdateEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the FirmwareUpdate in the database
        List<FirmwareUpdateEntity> firmwareUpdateList = firmwareUpdateRepository.findAll();
        assertThat(firmwareUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFirmwareUpdate() throws Exception {
        int databaseSizeBeforeUpdate = firmwareUpdateRepository.findAll().size();
        firmwareUpdateEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFirmwareUpdateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(firmwareUpdateEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the FirmwareUpdate in the database
        List<FirmwareUpdateEntity> firmwareUpdateList = firmwareUpdateRepository.findAll();
        assertThat(firmwareUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFirmwareUpdate() throws Exception {
        int databaseSizeBeforeUpdate = firmwareUpdateRepository.findAll().size();
        firmwareUpdateEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFirmwareUpdateMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(firmwareUpdateEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FirmwareUpdate in the database
        List<FirmwareUpdateEntity> firmwareUpdateList = firmwareUpdateRepository.findAll();
        assertThat(firmwareUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFirmwareUpdateWithPatch() throws Exception {
        // Initialize the database
        firmwareUpdateRepository.saveAndFlush(firmwareUpdateEntity);

        int databaseSizeBeforeUpdate = firmwareUpdateRepository.findAll().size();

        // Update the firmwareUpdate using partial update
        FirmwareUpdateEntity partialUpdatedFirmwareUpdateEntity = new FirmwareUpdateEntity();
        partialUpdatedFirmwareUpdateEntity.setId(firmwareUpdateEntity.getId());

        partialUpdatedFirmwareUpdateEntity.active(UPDATED_ACTIVE);

        restFirmwareUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFirmwareUpdateEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFirmwareUpdateEntity))
            )
            .andExpect(status().isOk());

        // Validate the FirmwareUpdate in the database
        List<FirmwareUpdateEntity> firmwareUpdateList = firmwareUpdateRepository.findAll();
        assertThat(firmwareUpdateList).hasSize(databaseSizeBeforeUpdate);
        FirmwareUpdateEntity testFirmwareUpdate = firmwareUpdateList.get(firmwareUpdateList.size() - 1);
        assertThat(testFirmwareUpdate.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateFirmwareUpdateWithPatch() throws Exception {
        // Initialize the database
        firmwareUpdateRepository.saveAndFlush(firmwareUpdateEntity);

        int databaseSizeBeforeUpdate = firmwareUpdateRepository.findAll().size();

        // Update the firmwareUpdate using partial update
        FirmwareUpdateEntity partialUpdatedFirmwareUpdateEntity = new FirmwareUpdateEntity();
        partialUpdatedFirmwareUpdateEntity.setId(firmwareUpdateEntity.getId());

        partialUpdatedFirmwareUpdateEntity.active(UPDATED_ACTIVE);

        restFirmwareUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFirmwareUpdateEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFirmwareUpdateEntity))
            )
            .andExpect(status().isOk());

        // Validate the FirmwareUpdate in the database
        List<FirmwareUpdateEntity> firmwareUpdateList = firmwareUpdateRepository.findAll();
        assertThat(firmwareUpdateList).hasSize(databaseSizeBeforeUpdate);
        FirmwareUpdateEntity testFirmwareUpdate = firmwareUpdateList.get(firmwareUpdateList.size() - 1);
        assertThat(testFirmwareUpdate.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingFirmwareUpdate() throws Exception {
        int databaseSizeBeforeUpdate = firmwareUpdateRepository.findAll().size();
        firmwareUpdateEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFirmwareUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, firmwareUpdateEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(firmwareUpdateEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the FirmwareUpdate in the database
        List<FirmwareUpdateEntity> firmwareUpdateList = firmwareUpdateRepository.findAll();
        assertThat(firmwareUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFirmwareUpdate() throws Exception {
        int databaseSizeBeforeUpdate = firmwareUpdateRepository.findAll().size();
        firmwareUpdateEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFirmwareUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(firmwareUpdateEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the FirmwareUpdate in the database
        List<FirmwareUpdateEntity> firmwareUpdateList = firmwareUpdateRepository.findAll();
        assertThat(firmwareUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFirmwareUpdate() throws Exception {
        int databaseSizeBeforeUpdate = firmwareUpdateRepository.findAll().size();
        firmwareUpdateEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFirmwareUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(firmwareUpdateEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FirmwareUpdate in the database
        List<FirmwareUpdateEntity> firmwareUpdateList = firmwareUpdateRepository.findAll();
        assertThat(firmwareUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFirmwareUpdate() throws Exception {
        // Initialize the database
        firmwareUpdateRepository.saveAndFlush(firmwareUpdateEntity);

        int databaseSizeBeforeDelete = firmwareUpdateRepository.findAll().size();

        // Delete the firmwareUpdate
        restFirmwareUpdateMockMvc
            .perform(delete(ENTITY_API_URL_ID, firmwareUpdateEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FirmwareUpdateEntity> firmwareUpdateList = firmwareUpdateRepository.findAll();
        assertThat(firmwareUpdateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
