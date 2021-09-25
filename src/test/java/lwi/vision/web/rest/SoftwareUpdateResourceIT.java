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
import lwi.vision.domain.SoftwareUpdate;
import lwi.vision.repository.SoftwareUpdateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SoftwareUpdateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SoftwareUpdateResourceIT {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/software-updates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SoftwareUpdateRepository softwareUpdateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSoftwareUpdateMockMvc;

    private SoftwareUpdate softwareUpdate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SoftwareUpdate createEntity(EntityManager em) {
        SoftwareUpdate softwareUpdate = new SoftwareUpdate().active(DEFAULT_ACTIVE);
        return softwareUpdate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SoftwareUpdate createUpdatedEntity(EntityManager em) {
        SoftwareUpdate softwareUpdate = new SoftwareUpdate().active(UPDATED_ACTIVE);
        return softwareUpdate;
    }

    @BeforeEach
    public void initTest() {
        softwareUpdate = createEntity(em);
    }

    @Test
    @Transactional
    void createSoftwareUpdate() throws Exception {
        int databaseSizeBeforeCreate = softwareUpdateRepository.findAll().size();
        // Create the SoftwareUpdate
        restSoftwareUpdateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(softwareUpdate))
            )
            .andExpect(status().isCreated());

        // Validate the SoftwareUpdate in the database
        List<SoftwareUpdate> softwareUpdateList = softwareUpdateRepository.findAll();
        assertThat(softwareUpdateList).hasSize(databaseSizeBeforeCreate + 1);
        SoftwareUpdate testSoftwareUpdate = softwareUpdateList.get(softwareUpdateList.size() - 1);
        assertThat(testSoftwareUpdate.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createSoftwareUpdateWithExistingId() throws Exception {
        // Create the SoftwareUpdate with an existing ID
        softwareUpdate.setId(1L);

        int databaseSizeBeforeCreate = softwareUpdateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSoftwareUpdateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(softwareUpdate))
            )
            .andExpect(status().isBadRequest());

        // Validate the SoftwareUpdate in the database
        List<SoftwareUpdate> softwareUpdateList = softwareUpdateRepository.findAll();
        assertThat(softwareUpdateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSoftwareUpdates() throws Exception {
        // Initialize the database
        softwareUpdateRepository.saveAndFlush(softwareUpdate);

        // Get all the softwareUpdateList
        restSoftwareUpdateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(softwareUpdate.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getSoftwareUpdate() throws Exception {
        // Initialize the database
        softwareUpdateRepository.saveAndFlush(softwareUpdate);

        // Get the softwareUpdate
        restSoftwareUpdateMockMvc
            .perform(get(ENTITY_API_URL_ID, softwareUpdate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(softwareUpdate.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSoftwareUpdate() throws Exception {
        // Get the softwareUpdate
        restSoftwareUpdateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSoftwareUpdate() throws Exception {
        // Initialize the database
        softwareUpdateRepository.saveAndFlush(softwareUpdate);

        int databaseSizeBeforeUpdate = softwareUpdateRepository.findAll().size();

        // Update the softwareUpdate
        SoftwareUpdate updatedSoftwareUpdate = softwareUpdateRepository.findById(softwareUpdate.getId()).get();
        // Disconnect from session so that the updates on updatedSoftwareUpdate are not directly saved in db
        em.detach(updatedSoftwareUpdate);
        updatedSoftwareUpdate.active(UPDATED_ACTIVE);

        restSoftwareUpdateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSoftwareUpdate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSoftwareUpdate))
            )
            .andExpect(status().isOk());

        // Validate the SoftwareUpdate in the database
        List<SoftwareUpdate> softwareUpdateList = softwareUpdateRepository.findAll();
        assertThat(softwareUpdateList).hasSize(databaseSizeBeforeUpdate);
        SoftwareUpdate testSoftwareUpdate = softwareUpdateList.get(softwareUpdateList.size() - 1);
        assertThat(testSoftwareUpdate.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingSoftwareUpdate() throws Exception {
        int databaseSizeBeforeUpdate = softwareUpdateRepository.findAll().size();
        softwareUpdate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSoftwareUpdateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, softwareUpdate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(softwareUpdate))
            )
            .andExpect(status().isBadRequest());

        // Validate the SoftwareUpdate in the database
        List<SoftwareUpdate> softwareUpdateList = softwareUpdateRepository.findAll();
        assertThat(softwareUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSoftwareUpdate() throws Exception {
        int databaseSizeBeforeUpdate = softwareUpdateRepository.findAll().size();
        softwareUpdate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoftwareUpdateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(softwareUpdate))
            )
            .andExpect(status().isBadRequest());

        // Validate the SoftwareUpdate in the database
        List<SoftwareUpdate> softwareUpdateList = softwareUpdateRepository.findAll();
        assertThat(softwareUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSoftwareUpdate() throws Exception {
        int databaseSizeBeforeUpdate = softwareUpdateRepository.findAll().size();
        softwareUpdate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoftwareUpdateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(softwareUpdate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SoftwareUpdate in the database
        List<SoftwareUpdate> softwareUpdateList = softwareUpdateRepository.findAll();
        assertThat(softwareUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSoftwareUpdateWithPatch() throws Exception {
        // Initialize the database
        softwareUpdateRepository.saveAndFlush(softwareUpdate);

        int databaseSizeBeforeUpdate = softwareUpdateRepository.findAll().size();

        // Update the softwareUpdate using partial update
        SoftwareUpdate partialUpdatedSoftwareUpdate = new SoftwareUpdate();
        partialUpdatedSoftwareUpdate.setId(softwareUpdate.getId());

        restSoftwareUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSoftwareUpdate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSoftwareUpdate))
            )
            .andExpect(status().isOk());

        // Validate the SoftwareUpdate in the database
        List<SoftwareUpdate> softwareUpdateList = softwareUpdateRepository.findAll();
        assertThat(softwareUpdateList).hasSize(databaseSizeBeforeUpdate);
        SoftwareUpdate testSoftwareUpdate = softwareUpdateList.get(softwareUpdateList.size() - 1);
        assertThat(testSoftwareUpdate.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateSoftwareUpdateWithPatch() throws Exception {
        // Initialize the database
        softwareUpdateRepository.saveAndFlush(softwareUpdate);

        int databaseSizeBeforeUpdate = softwareUpdateRepository.findAll().size();

        // Update the softwareUpdate using partial update
        SoftwareUpdate partialUpdatedSoftwareUpdate = new SoftwareUpdate();
        partialUpdatedSoftwareUpdate.setId(softwareUpdate.getId());

        partialUpdatedSoftwareUpdate.active(UPDATED_ACTIVE);

        restSoftwareUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSoftwareUpdate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSoftwareUpdate))
            )
            .andExpect(status().isOk());

        // Validate the SoftwareUpdate in the database
        List<SoftwareUpdate> softwareUpdateList = softwareUpdateRepository.findAll();
        assertThat(softwareUpdateList).hasSize(databaseSizeBeforeUpdate);
        SoftwareUpdate testSoftwareUpdate = softwareUpdateList.get(softwareUpdateList.size() - 1);
        assertThat(testSoftwareUpdate.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingSoftwareUpdate() throws Exception {
        int databaseSizeBeforeUpdate = softwareUpdateRepository.findAll().size();
        softwareUpdate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSoftwareUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, softwareUpdate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(softwareUpdate))
            )
            .andExpect(status().isBadRequest());

        // Validate the SoftwareUpdate in the database
        List<SoftwareUpdate> softwareUpdateList = softwareUpdateRepository.findAll();
        assertThat(softwareUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSoftwareUpdate() throws Exception {
        int databaseSizeBeforeUpdate = softwareUpdateRepository.findAll().size();
        softwareUpdate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoftwareUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(softwareUpdate))
            )
            .andExpect(status().isBadRequest());

        // Validate the SoftwareUpdate in the database
        List<SoftwareUpdate> softwareUpdateList = softwareUpdateRepository.findAll();
        assertThat(softwareUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSoftwareUpdate() throws Exception {
        int databaseSizeBeforeUpdate = softwareUpdateRepository.findAll().size();
        softwareUpdate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoftwareUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(softwareUpdate))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SoftwareUpdate in the database
        List<SoftwareUpdate> softwareUpdateList = softwareUpdateRepository.findAll();
        assertThat(softwareUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSoftwareUpdate() throws Exception {
        // Initialize the database
        softwareUpdateRepository.saveAndFlush(softwareUpdate);

        int databaseSizeBeforeDelete = softwareUpdateRepository.findAll().size();

        // Delete the softwareUpdate
        restSoftwareUpdateMockMvc
            .perform(delete(ENTITY_API_URL_ID, softwareUpdate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SoftwareUpdate> softwareUpdateList = softwareUpdateRepository.findAll();
        assertThat(softwareUpdateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
