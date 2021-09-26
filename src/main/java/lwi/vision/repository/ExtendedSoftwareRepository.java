package lwi.vision.repository;

import java.util.Optional;
import lwi.vision.domain.Software;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtendedSoftwareRepository extends SoftwareRepository {
    Optional<Software> findFirstByBoard_SerialIsOrderByCreatedDateAsc(String serial);
}
