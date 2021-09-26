package lwi.vision.repository;

import lwi.vision.domain.SoftwareEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExtendedSoftwareRepository extends SoftwareRepository {
    Optional<SoftwareEntity> findFirstByBoard_SerialIsOrderByCreatedDateDesc(String serial);

    List<SoftwareEntity> findByBoard_SerialIsOrderByCreatedDateDesc(String serial);
}
