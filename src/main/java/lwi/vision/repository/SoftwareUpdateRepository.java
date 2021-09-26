package lwi.vision.repository;

import lwi.vision.domain.SoftwareUpdateEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SoftwareUpdateEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SoftwareUpdateRepository extends JpaRepository<SoftwareUpdateEntity, Long> {}
