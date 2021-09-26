package lwi.vision.domain;

import static org.assertj.core.api.Assertions.assertThat;

import lwi.vision.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BoardEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardEntity.class);
        BoardEntity boardEntity1 = new BoardEntity();
        boardEntity1.setId(1L);
        BoardEntity boardEntity2 = new BoardEntity();
        boardEntity2.setId(boardEntity1.getId());
        assertThat(boardEntity1).isEqualTo(boardEntity2);
        boardEntity2.setId(2L);
        assertThat(boardEntity1).isNotEqualTo(boardEntity2);
        boardEntity1.setId(null);
        assertThat(boardEntity1).isNotEqualTo(boardEntity2);
    }
}
