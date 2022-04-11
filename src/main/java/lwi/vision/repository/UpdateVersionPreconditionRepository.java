package lwi.vision.repository;

import lwi.vision.domain.UpdateVersionPreconditionEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UpdateVersionPreconditionEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UpdateVersionPreconditionRepository extends JpaRepository<UpdateVersionPreconditionEntity, Long> {}
