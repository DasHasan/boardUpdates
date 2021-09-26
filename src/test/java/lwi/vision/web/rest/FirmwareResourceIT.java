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
import lwi.vision.domain.FirmwareEntity;
import lwi.vision.repository.FirmwareRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FirmwareResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FirmwareResourceIT {

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/firmware";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FirmwareRepository firmwareRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFirmwareMockMvc;

    private FirmwareEntity firmwareEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FirmwareEntity createEntity(EntityManager em) {
        FirmwareEntity firmwareEntity = new FirmwareEntity().version(DEFAULT_VERSION).path(DEFAULT_PATH);
        return firmwareEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FirmwareEntity createUpdatedEntity(EntityManager em) {
        FirmwareEntity firmwareEntity = new FirmwareEntity().version(UPDATED_VERSION).path(UPDATED_PATH);
        return firmwareEntity;
    }

    @BeforeEach
    public void initTest() {
        firmwareEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createFirmware() throws Exception {
        int databaseSizeBeforeCreate = firmwareRepository.findAll().size();
        // Create the Firmware
        restFirmwareMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(firmwareEntity))
            )
            .andExpect(status().isCreated());

        // Validate the Firmware in the database
        List<FirmwareEntity> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeCreate + 1);
        FirmwareEntity testFirmware = firmwareList.get(firmwareList.size() - 1);
        assertThat(testFirmware.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testFirmware.getPath()).isEqualTo(DEFAULT_PATH);
    }

    @Test
    @Transactional
    void createFirmwareWithExistingId() throws Exception {
        // Create the Firmware with an existing ID
        firmwareEntity.setId(1L);

        int databaseSizeBeforeCreate = firmwareRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFirmwareMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(firmwareEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Firmware in the database
        List<FirmwareEntity> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFirmware() throws Exception {
        // Initialize the database
        firmwareRepository.saveAndFlush(firmwareEntity);

        // Get all the firmwareList
        restFirmwareMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(firmwareEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)));
    }

    @Test
    @Transactional
    void getFirmware() throws Exception {
        // Initialize the database
        firmwareRepository.saveAndFlush(firmwareEntity);

        // Get the firmware
        restFirmwareMockMvc
            .perform(get(ENTITY_API_URL_ID, firmwareEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(firmwareEntity.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH));
    }

    @Test
    @Transactional
    void getNonExistingFirmware() throws Exception {
        // Get the firmware
        restFirmwareMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFirmware() throws Exception {
        // Initialize the database
        firmwareRepository.saveAndFlush(firmwareEntity);

        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();

        // Update the firmware
        FirmwareEntity updatedFirmwareEntity = firmwareRepository.findById(firmwareEntity.getId()).get();
        // Disconnect from session so that the updates on updatedFirmwareEntity are not directly saved in db
        em.detach(updatedFirmwareEntity);
        updatedFirmwareEntity.version(UPDATED_VERSION).path(UPDATED_PATH);

        restFirmwareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFirmwareEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFirmwareEntity))
            )
            .andExpect(status().isOk());

        // Validate the Firmware in the database
        List<FirmwareEntity> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
        FirmwareEntity testFirmware = firmwareList.get(firmwareList.size() - 1);
        assertThat(testFirmware.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testFirmware.getPath()).isEqualTo(UPDATED_PATH);
    }

    @Test
    @Transactional
    void putNonExistingFirmware() throws Exception {
        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();
        firmwareEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFirmwareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, firmwareEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(firmwareEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Firmware in the database
        List<FirmwareEntity> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFirmware() throws Exception {
        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();
        firmwareEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFirmwareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(firmwareEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Firmware in the database
        List<FirmwareEntity> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFirmware() throws Exception {
        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();
        firmwareEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFirmwareMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(firmwareEntity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Firmware in the database
        List<FirmwareEntity> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFirmwareWithPatch() throws Exception {
        // Initialize the database
        firmwareRepository.saveAndFlush(firmwareEntity);

        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();

        // Update the firmware using partial update
        FirmwareEntity partialUpdatedFirmwareEntity = new FirmwareEntity();
        partialUpdatedFirmwareEntity.setId(firmwareEntity.getId());

        partialUpdatedFirmwareEntity.path(UPDATED_PATH);

        restFirmwareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFirmwareEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFirmwareEntity))
            )
            .andExpect(status().isOk());

        // Validate the Firmware in the database
        List<FirmwareEntity> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
        FirmwareEntity testFirmware = firmwareList.get(firmwareList.size() - 1);
        assertThat(testFirmware.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testFirmware.getPath()).isEqualTo(UPDATED_PATH);
    }

    @Test
    @Transactional
    void fullUpdateFirmwareWithPatch() throws Exception {
        // Initialize the database
        firmwareRepository.saveAndFlush(firmwareEntity);

        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();

        // Update the firmware using partial update
        FirmwareEntity partialUpdatedFirmwareEntity = new FirmwareEntity();
        partialUpdatedFirmwareEntity.setId(firmwareEntity.getId());

        partialUpdatedFirmwareEntity.version(UPDATED_VERSION).path(UPDATED_PATH);

        restFirmwareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFirmwareEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFirmwareEntity))
            )
            .andExpect(status().isOk());

        // Validate the Firmware in the database
        List<FirmwareEntity> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
        FirmwareEntity testFirmware = firmwareList.get(firmwareList.size() - 1);
        assertThat(testFirmware.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testFirmware.getPath()).isEqualTo(UPDATED_PATH);
    }

    @Test
    @Transactional
    void patchNonExistingFirmware() throws Exception {
        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();
        firmwareEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFirmwareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, firmwareEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(firmwareEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Firmware in the database
        List<FirmwareEntity> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFirmware() throws Exception {
        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();
        firmwareEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFirmwareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(firmwareEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Firmware in the database
        List<FirmwareEntity> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFirmware() throws Exception {
        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();
        firmwareEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFirmwareMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(firmwareEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Firmware in the database
        List<FirmwareEntity> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFirmware() throws Exception {
        // Initialize the database
        firmwareRepository.saveAndFlush(firmwareEntity);

        int databaseSizeBeforeDelete = firmwareRepository.findAll().size();

        // Delete the firmware
        restFirmwareMockMvc
            .perform(delete(ENTITY_API_URL_ID, firmwareEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FirmwareEntity> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
