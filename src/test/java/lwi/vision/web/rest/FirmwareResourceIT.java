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
import lwi.vision.domain.Firmware;
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

    private Firmware firmware;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Firmware createEntity(EntityManager em) {
        Firmware firmware = new Firmware().version(DEFAULT_VERSION).path(DEFAULT_PATH);
        return firmware;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Firmware createUpdatedEntity(EntityManager em) {
        Firmware firmware = new Firmware().version(UPDATED_VERSION).path(UPDATED_PATH);
        return firmware;
    }

    @BeforeEach
    public void initTest() {
        firmware = createEntity(em);
    }

    @Test
    @Transactional
    void createFirmware() throws Exception {
        int databaseSizeBeforeCreate = firmwareRepository.findAll().size();
        // Create the Firmware
        restFirmwareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(firmware)))
            .andExpect(status().isCreated());

        // Validate the Firmware in the database
        List<Firmware> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeCreate + 1);
        Firmware testFirmware = firmwareList.get(firmwareList.size() - 1);
        assertThat(testFirmware.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testFirmware.getPath()).isEqualTo(DEFAULT_PATH);
    }

    @Test
    @Transactional
    void createFirmwareWithExistingId() throws Exception {
        // Create the Firmware with an existing ID
        firmware.setId(1L);

        int databaseSizeBeforeCreate = firmwareRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFirmwareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(firmware)))
            .andExpect(status().isBadRequest());

        // Validate the Firmware in the database
        List<Firmware> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFirmware() throws Exception {
        // Initialize the database
        firmwareRepository.saveAndFlush(firmware);

        // Get all the firmwareList
        restFirmwareMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(firmware.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)));
    }

    @Test
    @Transactional
    void getFirmware() throws Exception {
        // Initialize the database
        firmwareRepository.saveAndFlush(firmware);

        // Get the firmware
        restFirmwareMockMvc
            .perform(get(ENTITY_API_URL_ID, firmware.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(firmware.getId().intValue()))
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
        firmwareRepository.saveAndFlush(firmware);

        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();

        // Update the firmware
        Firmware updatedFirmware = firmwareRepository.findById(firmware.getId()).get();
        // Disconnect from session so that the updates on updatedFirmware are not directly saved in db
        em.detach(updatedFirmware);
        updatedFirmware.version(UPDATED_VERSION).path(UPDATED_PATH);

        restFirmwareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFirmware.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFirmware))
            )
            .andExpect(status().isOk());

        // Validate the Firmware in the database
        List<Firmware> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
        Firmware testFirmware = firmwareList.get(firmwareList.size() - 1);
        assertThat(testFirmware.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testFirmware.getPath()).isEqualTo(UPDATED_PATH);
    }

    @Test
    @Transactional
    void putNonExistingFirmware() throws Exception {
        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();
        firmware.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFirmwareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, firmware.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(firmware))
            )
            .andExpect(status().isBadRequest());

        // Validate the Firmware in the database
        List<Firmware> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFirmware() throws Exception {
        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();
        firmware.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFirmwareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(firmware))
            )
            .andExpect(status().isBadRequest());

        // Validate the Firmware in the database
        List<Firmware> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFirmware() throws Exception {
        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();
        firmware.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFirmwareMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(firmware)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Firmware in the database
        List<Firmware> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFirmwareWithPatch() throws Exception {
        // Initialize the database
        firmwareRepository.saveAndFlush(firmware);

        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();

        // Update the firmware using partial update
        Firmware partialUpdatedFirmware = new Firmware();
        partialUpdatedFirmware.setId(firmware.getId());

        partialUpdatedFirmware.path(UPDATED_PATH);

        restFirmwareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFirmware.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFirmware))
            )
            .andExpect(status().isOk());

        // Validate the Firmware in the database
        List<Firmware> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
        Firmware testFirmware = firmwareList.get(firmwareList.size() - 1);
        assertThat(testFirmware.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testFirmware.getPath()).isEqualTo(UPDATED_PATH);
    }

    @Test
    @Transactional
    void fullUpdateFirmwareWithPatch() throws Exception {
        // Initialize the database
        firmwareRepository.saveAndFlush(firmware);

        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();

        // Update the firmware using partial update
        Firmware partialUpdatedFirmware = new Firmware();
        partialUpdatedFirmware.setId(firmware.getId());

        partialUpdatedFirmware.version(UPDATED_VERSION).path(UPDATED_PATH);

        restFirmwareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFirmware.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFirmware))
            )
            .andExpect(status().isOk());

        // Validate the Firmware in the database
        List<Firmware> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
        Firmware testFirmware = firmwareList.get(firmwareList.size() - 1);
        assertThat(testFirmware.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testFirmware.getPath()).isEqualTo(UPDATED_PATH);
    }

    @Test
    @Transactional
    void patchNonExistingFirmware() throws Exception {
        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();
        firmware.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFirmwareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, firmware.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(firmware))
            )
            .andExpect(status().isBadRequest());

        // Validate the Firmware in the database
        List<Firmware> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFirmware() throws Exception {
        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();
        firmware.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFirmwareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(firmware))
            )
            .andExpect(status().isBadRequest());

        // Validate the Firmware in the database
        List<Firmware> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFirmware() throws Exception {
        int databaseSizeBeforeUpdate = firmwareRepository.findAll().size();
        firmware.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFirmwareMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(firmware)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Firmware in the database
        List<Firmware> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFirmware() throws Exception {
        // Initialize the database
        firmwareRepository.saveAndFlush(firmware);

        int databaseSizeBeforeDelete = firmwareRepository.findAll().size();

        // Delete the firmware
        restFirmwareMockMvc
            .perform(delete(ENTITY_API_URL_ID, firmware.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Firmware> firmwareList = firmwareRepository.findAll();
        assertThat(firmwareList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
