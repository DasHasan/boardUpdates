package lwi.vision.repository;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.FirmwareEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtendedFirmwareRepository extends FirmwareRepository {
    Optional<FirmwareEntity> findFirstByBoard_SerialIsOrderByCreatedDateDesc(String serial);

    List<FirmwareEntity> findByBoard_SerialIsOrderByCreatedDateDesc(String serial);
}
