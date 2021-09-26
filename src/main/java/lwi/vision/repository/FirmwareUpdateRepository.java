package lwi.vision.repository;

import lwi.vision.domain.FirmwareUpdateEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FirmwareUpdateEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FirmwareUpdateRepository extends JpaRepository<FirmwareUpdateEntity, Long> {}
