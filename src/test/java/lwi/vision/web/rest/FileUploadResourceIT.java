package lwi.vision.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lwi.vision.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for the FileUploadResource REST controller.
 *
 * @see FileUploadResource
 */
@IntegrationTest
class FileUploadResourceIT {

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        FileUploadResource fileUploadResource = new FileUploadResource();
        restMockMvc = MockMvcBuilders.standaloneSetup(fileUploadResource).build();
    }

    /**
     * Test uploadSoftware
     */
    @Test
    void testUploadSoftware() throws Exception {
        restMockMvc.perform(post("/api/file-upload/upload-software")).andExpect(status().isOk());
    }

    /**
     * Test uploadFirmware
     */
    @Test
    void testUploadFirmware() throws Exception {
        restMockMvc.perform(post("/api/file-upload/upload-firmware")).andExpect(status().isOk());
    }

    /**
     * Test a
     */
    @Test
    void testA() throws Exception {
        restMockMvc.perform(get("/api/file-upload/a")).andExpect(status().isOk());
    }
}
