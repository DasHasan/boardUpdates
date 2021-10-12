package lwi.vision.repository;

import lwi.vision.domain.UpdateKeysEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UpdateKeysEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UpdateKeysRepository extends JpaRepository<UpdateKeysEntity, Long>, JpaSpecificationExecutor<UpdateKeysEntity> {}
