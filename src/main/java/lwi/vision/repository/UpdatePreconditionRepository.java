package lwi.vision.repository;

import lwi.vision.domain.UpdatePreconditionEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UpdatePreconditionEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UpdatePreconditionRepository extends JpaRepository<UpdatePreconditionEntity, Long> {}
