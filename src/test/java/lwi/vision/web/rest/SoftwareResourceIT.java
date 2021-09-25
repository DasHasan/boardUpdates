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
import lwi.vision.domain.Software;
import lwi.vision.repository.SoftwareRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SoftwareResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SoftwareResourceIT {

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/software";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SoftwareRepository softwareRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSoftwareMockMvc;

    private Software software;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Software createEntity(EntityManager em) {
        Software software = new Software().version(DEFAULT_VERSION).path(DEFAULT_PATH);
        return software;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Software createUpdatedEntity(EntityManager em) {
        Software software = new Software().version(UPDATED_VERSION).path(UPDATED_PATH);
        return software;
    }

    @BeforeEach
    public void initTest() {
        software = createEntity(em);
    }

    @Test
    @Transactional
    void createSoftware() throws Exception {
        int databaseSizeBeforeCreate = softwareRepository.findAll().size();
        // Create the Software
        restSoftwareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(software)))
            .andExpect(status().isCreated());

        // Validate the Software in the database
        List<Software> softwareList = softwareRepository.findAll();
        assertThat(softwareList).hasSize(databaseSizeBeforeCreate + 1);
        Software testSoftware = softwareList.get(softwareList.size() - 1);
        assertThat(testSoftware.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testSoftware.getPath()).isEqualTo(DEFAULT_PATH);
    }

    @Test
    @Transactional
    void createSoftwareWithExistingId() throws Exception {
        // Create the Software with an existing ID
        software.setId(1L);

        int databaseSizeBeforeCreate = softwareRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSoftwareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(software)))
            .andExpect(status().isBadRequest());

        // Validate the Software in the database
        List<Software> softwareList = softwareRepository.findAll();
        assertThat(softwareList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSoftware() throws Exception {
        // Initialize the database
        softwareRepository.saveAndFlush(software);

        // Get all the softwareList
        restSoftwareMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(software.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)));
    }

    @Test
    @Transactional
    void getSoftware() throws Exception {
        // Initialize the database
        softwareRepository.saveAndFlush(software);

        // Get the software
        restSoftwareMockMvc
            .perform(get(ENTITY_API_URL_ID, software.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(software.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH));
    }

    @Test
    @Transactional
    void getNonExistingSoftware() throws Exception {
        // Get the software
        restSoftwareMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSoftware() throws Exception {
        // Initialize the database
        softwareRepository.saveAndFlush(software);

        int databaseSizeBeforeUpdate = softwareRepository.findAll().size();

        // Update the software
        Software updatedSoftware = softwareRepository.findById(software.getId()).get();
        // Disconnect from session so that the updates on updatedSoftware are not directly saved in db
        em.detach(updatedSoftware);
        updatedSoftware.version(UPDATED_VERSION).path(UPDATED_PATH);

        restSoftwareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSoftware.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSoftware))
            )
            .andExpect(status().isOk());

        // Validate the Software in the database
        List<Software> softwareList = softwareRepository.findAll();
        assertThat(softwareList).hasSize(databaseSizeBeforeUpdate);
        Software testSoftware = softwareList.get(softwareList.size() - 1);
        assertThat(testSoftware.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testSoftware.getPath()).isEqualTo(UPDATED_PATH);
    }

    @Test
    @Transactional
    void putNonExistingSoftware() throws Exception {
        int databaseSizeBeforeUpdate = softwareRepository.findAll().size();
        software.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSoftwareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, software.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(software))
            )
            .andExpect(status().isBadRequest());

        // Validate the Software in the database
        List<Software> softwareList = softwareRepository.findAll();
        assertThat(softwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSoftware() throws Exception {
        int databaseSizeBeforeUpdate = softwareRepository.findAll().size();
        software.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoftwareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(software))
            )
            .andExpect(status().isBadRequest());

        // Validate the Software in the database
        List<Software> softwareList = softwareRepository.findAll();
        assertThat(softwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSoftware() throws Exception {
        int databaseSizeBeforeUpdate = softwareRepository.findAll().size();
        software.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoftwareMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(software)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Software in the database
        List<Software> softwareList = softwareRepository.findAll();
        assertThat(softwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSoftwareWithPatch() throws Exception {
        // Initialize the database
        softwareRepository.saveAndFlush(software);

        int databaseSizeBeforeUpdate = softwareRepository.findAll().size();

        // Update the software using partial update
        Software partialUpdatedSoftware = new Software();
        partialUpdatedSoftware.setId(software.getId());

        partialUpdatedSoftware.path(UPDATED_PATH);

        restSoftwareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSoftware.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSoftware))
            )
            .andExpect(status().isOk());

        // Validate the Software in the database
        List<Software> softwareList = softwareRepository.findAll();
        assertThat(softwareList).hasSize(databaseSizeBeforeUpdate);
        Software testSoftware = softwareList.get(softwareList.size() - 1);
        assertThat(testSoftware.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testSoftware.getPath()).isEqualTo(UPDATED_PATH);
    }

    @Test
    @Transactional
    void fullUpdateSoftwareWithPatch() throws Exception {
        // Initialize the database
        softwareRepository.saveAndFlush(software);

        int databaseSizeBeforeUpdate = softwareRepository.findAll().size();

        // Update the software using partial update
        Software partialUpdatedSoftware = new Software();
        partialUpdatedSoftware.setId(software.getId());

        partialUpdatedSoftware.version(UPDATED_VERSION).path(UPDATED_PATH);

        restSoftwareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSoftware.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSoftware))
            )
            .andExpect(status().isOk());

        // Validate the Software in the database
        List<Software> softwareList = softwareRepository.findAll();
        assertThat(softwareList).hasSize(databaseSizeBeforeUpdate);
        Software testSoftware = softwareList.get(softwareList.size() - 1);
        assertThat(testSoftware.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testSoftware.getPath()).isEqualTo(UPDATED_PATH);
    }

    @Test
    @Transactional
    void patchNonExistingSoftware() throws Exception {
        int databaseSizeBeforeUpdate = softwareRepository.findAll().size();
        software.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSoftwareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, software.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(software))
            )
            .andExpect(status().isBadRequest());

        // Validate the Software in the database
        List<Software> softwareList = softwareRepository.findAll();
        assertThat(softwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSoftware() throws Exception {
        int databaseSizeBeforeUpdate = softwareRepository.findAll().size();
        software.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoftwareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(software))
            )
            .andExpect(status().isBadRequest());

        // Validate the Software in the database
        List<Software> softwareList = softwareRepository.findAll();
        assertThat(softwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSoftware() throws Exception {
        int databaseSizeBeforeUpdate = softwareRepository.findAll().size();
        software.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoftwareMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(software)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Software in the database
        List<Software> softwareList = softwareRepository.findAll();
        assertThat(softwareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSoftware() throws Exception {
        // Initialize the database
        softwareRepository.saveAndFlush(software);

        int databaseSizeBeforeDelete = softwareRepository.findAll().size();

        // Delete the software
        restSoftwareMockMvc
            .perform(delete(ENTITY_API_URL_ID, software.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Software> softwareList = softwareRepository.findAll();
        assertThat(softwareList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
