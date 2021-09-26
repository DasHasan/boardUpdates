package lwi.vision.repository;

import java.util.Optional;
import lwi.vision.domain.BoardEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface MyBoardRepository extends BoardRepository {
    Optional<BoardEntity> findBySerialIs(String serial);
}
