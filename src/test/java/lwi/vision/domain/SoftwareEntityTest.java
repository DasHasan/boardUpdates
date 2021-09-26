package lwi.vision.domain;

import static org.assertj.core.api.Assertions.assertThat;

import lwi.vision.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SoftwareEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SoftwareEntity.class);
        SoftwareEntity softwareEntity1 = new SoftwareEntity();
        softwareEntity1.setId(1L);
        SoftwareEntity softwareEntity2 = new SoftwareEntity();
        softwareEntity2.setId(softwareEntity1.getId());
        assertThat(softwareEntity1).isEqualTo(softwareEntity2);
        softwareEntity2.setId(2L);
        assertThat(softwareEntity1).isNotEqualTo(softwareEntity2);
        softwareEntity1.setId(null);
        assertThat(softwareEntity1).isNotEqualTo(softwareEntity2);
    }
}
