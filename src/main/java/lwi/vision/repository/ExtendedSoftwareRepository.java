package lwi.vision.repository;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.SoftwareEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtendedSoftwareRepository extends SoftwareRepository {
    Optional<SoftwareEntity> findFirstByBoard_SerialIsOrderByCreatedDateDesc(String serial);

    List<SoftwareEntity> findByBoard_SerialIsOrderByCreatedDateDesc(String serial);
}
