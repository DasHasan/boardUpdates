package lwi.vision.repository;

import java.util.Optional;
import lwi.vision.domain.SoftwareUpdate;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtendedSoftwareUpdateRepository extends SoftwareUpdateRepository {
    Optional<SoftwareUpdate> findByBoard_SerialAndFrom_Version(String serial, String softwareVersion);
}
