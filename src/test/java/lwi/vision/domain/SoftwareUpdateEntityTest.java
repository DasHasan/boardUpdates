package lwi.vision.domain;

import static org.assertj.core.api.Assertions.assertThat;

import lwi.vision.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SoftwareUpdateEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SoftwareUpdateEntity.class);
        SoftwareUpdateEntity softwareUpdateEntity1 = new SoftwareUpdateEntity();
        softwareUpdateEntity1.setId(1L);
        SoftwareUpdateEntity softwareUpdateEntity2 = new SoftwareUpdateEntity();
        softwareUpdateEntity2.setId(softwareUpdateEntity1.getId());
        assertThat(softwareUpdateEntity1).isEqualTo(softwareUpdateEntity2);
        softwareUpdateEntity2.setId(2L);
        assertThat(softwareUpdateEntity1).isNotEqualTo(softwareUpdateEntity2);
        softwareUpdateEntity1.setId(null);
        assertThat(softwareUpdateEntity1).isNotEqualTo(softwareUpdateEntity2);
    }
}
