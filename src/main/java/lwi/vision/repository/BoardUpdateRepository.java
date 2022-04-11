package lwi.vision.repository;

import lwi.vision.domain.BoardUpdateEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BoardUpdateEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoardUpdateRepository extends JpaRepository<BoardUpdateEntity, Long>, JpaSpecificationExecutor<BoardUpdateEntity> {}
