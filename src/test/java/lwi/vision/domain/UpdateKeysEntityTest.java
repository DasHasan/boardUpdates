package lwi.vision.domain;

import static org.assertj.core.api.Assertions.assertThat;

import lwi.vision.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UpdateKeysEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UpdateKeysEntity.class);
        UpdateKeysEntity updateKeysEntity1 = new UpdateKeysEntity();
        updateKeysEntity1.setId(1L);
        UpdateKeysEntity updateKeysEntity2 = new UpdateKeysEntity();
        updateKeysEntity2.setId(updateKeysEntity1.getId());
        assertThat(updateKeysEntity1).isEqualTo(updateKeysEntity2);
        updateKeysEntity2.setId(2L);
        assertThat(updateKeysEntity1).isNotEqualTo(updateKeysEntity2);
        updateKeysEntity1.setId(null);
        assertThat(updateKeysEntity1).isNotEqualTo(updateKeysEntity2);
    }
}
