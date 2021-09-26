package lwi.vision.repository;

import java.util.Optional;
import lwi.vision.domain.Firmware;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtendedFirmwareRepository extends FirmwareRepository {
    Optional<Firmware> findFirstByBoard_SerialIsOrderByCreatedDateAsc(String serial);
}
