package lwi.vision.repository;

import lwi.vision.domain.SoftwareEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SoftwareEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SoftwareRepository extends JpaRepository<SoftwareEntity, Long> {}
