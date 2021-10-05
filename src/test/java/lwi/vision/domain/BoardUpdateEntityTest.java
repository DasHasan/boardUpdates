package lwi.vision.domain;

import static org.assertj.core.api.Assertions.assertThat;

import lwi.vision.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BoardUpdateEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardUpdateEntity.class);
        BoardUpdateEntity boardUpdateEntity1 = new BoardUpdateEntity();
        boardUpdateEntity1.setId(1L);
        BoardUpdateEntity boardUpdateEntity2 = new BoardUpdateEntity();
        boardUpdateEntity2.setId(boardUpdateEntity1.getId());
        assertThat(boardUpdateEntity1).isEqualTo(boardUpdateEntity2);
        boardUpdateEntity2.setId(2L);
        assertThat(boardUpdateEntity1).isNotEqualTo(boardUpdateEntity2);
        boardUpdateEntity1.setId(null);
        assertThat(boardUpdateEntity1).isNotEqualTo(boardUpdateEntity2);
    }
}
