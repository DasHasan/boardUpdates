package lwi.vision.domain;

import static org.assertj.core.api.Assertions.assertThat;

import lwi.vision.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SoftwareUpdateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SoftwareUpdate.class);
        SoftwareUpdate softwareUpdate1 = new SoftwareUpdate();
        softwareUpdate1.setId(1L);
        SoftwareUpdate softwareUpdate2 = new SoftwareUpdate();
        softwareUpdate2.setId(softwareUpdate1.getId());
        assertThat(softwareUpdate1).isEqualTo(softwareUpdate2);
        softwareUpdate2.setId(2L);
        assertThat(softwareUpdate1).isNotEqualTo(softwareUpdate2);
        softwareUpdate1.setId(null);
        assertThat(softwareUpdate1).isNotEqualTo(softwareUpdate2);
    }
}
