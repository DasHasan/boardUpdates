package lwi.vision.repository;

import lwi.vision.domain.DownloadUrlEntity;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DownloadUrlQueryRepository extends DownloadUrlRepository, QuerydslPredicateExecutor<DownloadUrlEntity> {
}
