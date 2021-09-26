package lwi.vision.repository;

import lwi.vision.domain.FirmwareUpdateEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExtendedFirmwareUpdateRepository extends FirmwareUpdateRepository {
    Optional<FirmwareUpdateEntity> findByBoard_SerialIsAndFrom_VersionIs(String serial, String version);
}
