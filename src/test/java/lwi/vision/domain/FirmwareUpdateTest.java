package lwi.vision.domain;

import static org.assertj.core.api.Assertions.assertThat;

import lwi.vision.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FirmwareUpdateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FirmwareUpdate.class);
        FirmwareUpdate firmwareUpdate1 = new FirmwareUpdate();
        firmwareUpdate1.setId(1L);
        FirmwareUpdate firmwareUpdate2 = new FirmwareUpdate();
        firmwareUpdate2.setId(firmwareUpdate1.getId());
        assertThat(firmwareUpdate1).isEqualTo(firmwareUpdate2);
        firmwareUpdate2.setId(2L);
        assertThat(firmwareUpdate1).isNotEqualTo(firmwareUpdate2);
        firmwareUpdate1.setId(null);
        assertThat(firmwareUpdate1).isNotEqualTo(firmwareUpdate2);
    }
}
