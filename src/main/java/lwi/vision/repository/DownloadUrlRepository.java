package lwi.vision.repository;

import java.util.Optional;
import lwi.vision.domain.DownloadUrlEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DownloadUrlEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DownloadUrlRepository extends JpaRepository<DownloadUrlEntity, Long>, JpaSpecificationExecutor<DownloadUrlEntity>, QuerydslPredicateExecutor<DownloadUrlEntity> {
    Optional<DownloadUrlEntity> findFirstByUrl(String url);
    Optional<DownloadUrlEntity> findFirstByBoardUpdate_Id(Long id);
}
