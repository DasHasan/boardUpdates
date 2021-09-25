package lwi.vision.domain;

import static org.assertj.core.api.Assertions.assertThat;

import lwi.vision.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SoftwareTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Software.class);
        Software software1 = new Software();
        software1.setId(1L);
        Software software2 = new Software();
        software2.setId(software1.getId());
        assertThat(software1).isEqualTo(software2);
        software2.setId(2L);
        assertThat(software1).isNotEqualTo(software2);
        software1.setId(null);
        assertThat(software1).isNotEqualTo(software2);
    }
}
