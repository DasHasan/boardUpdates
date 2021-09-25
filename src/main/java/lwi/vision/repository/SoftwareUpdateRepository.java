package lwi.vision.repository;

import lwi.vision.domain.SoftwareUpdate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SoftwareUpdate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SoftwareUpdateRepository extends JpaRepository<SoftwareUpdate, Long> {}
