package lwi.vision.repository;

import java.util.Optional;

import lwi.vision.domain.SoftwareUpdateEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtendedSoftwareUpdateRepository extends SoftwareUpdateRepository {
    Optional<SoftwareUpdateEntity> findByBoard_SerialAndFrom_Version(String serial, String softwareVersion);
}
