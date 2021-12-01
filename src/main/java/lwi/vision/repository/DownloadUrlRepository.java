package lwi.vision.repository;

import lwi.vision.domain.DownloadUrlEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DownloadUrlEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DownloadUrlRepository extends JpaRepository<DownloadUrlEntity, Long>, JpaSpecificationExecutor<DownloadUrlEntity> {}
