package lwi.vision.domain;

import static org.assertj.core.api.Assertions.assertThat;

import lwi.vision.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GroupEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupEntity.class);
        GroupEntity groupEntity1 = new GroupEntity();
        groupEntity1.setId(1L);
        GroupEntity groupEntity2 = new GroupEntity();
        groupEntity2.setId(groupEntity1.getId());
        assertThat(groupEntity1).isEqualTo(groupEntity2);
        groupEntity2.setId(2L);
        assertThat(groupEntity1).isNotEqualTo(groupEntity2);
        groupEntity1.setId(null);
        assertThat(groupEntity1).isNotEqualTo(groupEntity2);
    }
}
