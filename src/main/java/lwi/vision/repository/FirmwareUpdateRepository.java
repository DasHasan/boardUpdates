package lwi.vision.repository;

import lwi.vision.domain.FirmwareUpdate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FirmwareUpdate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FirmwareUpdateRepository extends JpaRepository<FirmwareUpdate, Long> {}
