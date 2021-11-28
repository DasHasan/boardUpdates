package lwi.vision.repository;

import lwi.vision.domain.BoardUpdateSuccessorEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BoardUpdateSuccessorEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoardUpdateSuccessorRepository
    extends JpaRepository<BoardUpdateSuccessorEntity, Long>, JpaSpecificationExecutor<BoardUpdateSuccessorEntity> {}
