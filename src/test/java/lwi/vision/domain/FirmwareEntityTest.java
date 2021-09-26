package lwi.vision.domain;

import static org.assertj.core.api.Assertions.assertThat;

import lwi.vision.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FirmwareEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FirmwareEntity.class);
        FirmwareEntity firmwareEntity1 = new FirmwareEntity();
        firmwareEntity1.setId(1L);
        FirmwareEntity firmwareEntity2 = new FirmwareEntity();
        firmwareEntity2.setId(firmwareEntity1.getId());
        assertThat(firmwareEntity1).isEqualTo(firmwareEntity2);
        firmwareEntity2.setId(2L);
        assertThat(firmwareEntity1).isNotEqualTo(firmwareEntity2);
        firmwareEntity1.setId(null);
        assertThat(firmwareEntity1).isNotEqualTo(firmwareEntity2);
    }
}
