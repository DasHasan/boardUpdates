package lwi.vision.repository;

import lwi.vision.domain.Firmware;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Firmware entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FirmwareRepository extends JpaRepository<Firmware, Long> {}
