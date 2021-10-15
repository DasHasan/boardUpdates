package lwi.vision.repository;

import java.util.Optional;
import lwi.vision.domain.BoardUpdateEntity;
import lwi.vision.domain.enumeration.UpdateType;
import org.springframework.data.jpa.repository.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BoardUpdateEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoardUpdateRepository extends JpaRepository<BoardUpdateEntity, Long>, JpaSpecificationExecutor<BoardUpdateEntity> {
    @Nullable
    Optional<BoardUpdateEntity> findByBoard_SerialAndVersionAndType(String serial, String version, UpdateType type);
}
