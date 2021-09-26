package lwi.vision.domain;

import static org.assertj.core.api.Assertions.assertThat;

import lwi.vision.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FirmwareUpdateEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FirmwareUpdateEntity.class);
        FirmwareUpdateEntity firmwareUpdateEntity1 = new FirmwareUpdateEntity();
        firmwareUpdateEntity1.setId(1L);
        FirmwareUpdateEntity firmwareUpdateEntity2 = new FirmwareUpdateEntity();
        firmwareUpdateEntity2.setId(firmwareUpdateEntity1.getId());
        assertThat(firmwareUpdateEntity1).isEqualTo(firmwareUpdateEntity2);
        firmwareUpdateEntity2.setId(2L);
        assertThat(firmwareUpdateEntity1).isNotEqualTo(firmwareUpdateEntity2);
        firmwareUpdateEntity1.setId(null);
        assertThat(firmwareUpdateEntity1).isNotEqualTo(firmwareUpdateEntity2);
    }
}
