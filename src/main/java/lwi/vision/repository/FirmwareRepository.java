package lwi.vision.repository;

import lwi.vision.domain.FirmwareEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FirmwareEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FirmwareRepository extends JpaRepository<FirmwareEntity, Long> {}
