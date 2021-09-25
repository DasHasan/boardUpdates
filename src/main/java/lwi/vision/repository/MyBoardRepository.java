package lwi.vision.repository;

import java.util.Optional;
import lwi.vision.domain.Board;
import org.springframework.stereotype.Repository;

@Repository
public interface MyBoardRepository extends BoardRepository {
    Optional<Board> findBySerialIs(String serial);
}
