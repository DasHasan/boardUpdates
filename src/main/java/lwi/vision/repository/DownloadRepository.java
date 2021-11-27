package lwi.vision.repository;

import lwi.vision.domain.DownloadEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DownloadEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DownloadRepository extends JpaRepository<DownloadEntity, Long>, JpaSpecificationExecutor<DownloadEntity> {}
