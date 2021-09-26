package lwi.vision.repository;

import lwi.vision.domain.GroupEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GroupEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {}
