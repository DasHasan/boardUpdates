package lwi.vision.domain;

import static org.assertj.core.api.Assertions.assertThat;

import lwi.vision.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FirmwareTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Firmware.class);
        Firmware firmware1 = new Firmware();
        firmware1.setId(1L);
        Firmware firmware2 = new Firmware();
        firmware2.setId(firmware1.getId());
        assertThat(firmware1).isEqualTo(firmware2);
        firmware2.setId(2L);
        assertThat(firmware1).isNotEqualTo(firmware2);
        firmware1.setId(null);
        assertThat(firmware1).isNotEqualTo(firmware2);
    }
}
