package lwi.vision.repository;

import java.util.Optional;
import lwi.vision.domain.BoardUpdateEntity;
import lwi.vision.domain.BoardUpdateSuccessorEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BoardUpdateSuccessorEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoardUpdateSuccessorRepository
    extends JpaRepository<BoardUpdateSuccessorEntity, Long>, JpaSpecificationExecutor<BoardUpdateSuccessorEntity> {
    Optional<BoardUpdateSuccessorEntity> findFirstByFrom_Id(Long id);
}
