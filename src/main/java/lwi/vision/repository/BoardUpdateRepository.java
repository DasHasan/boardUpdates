package lwi.vision.repository;

import java.util.List;
import lwi.vision.domain.BoardUpdateEntity;
import lwi.vision.domain.enumeration.UpdateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BoardUpdateEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoardUpdateRepository extends JpaRepository<BoardUpdateEntity, Long>, JpaSpecificationExecutor<BoardUpdateEntity>, QuerydslPredicateExecutor<BoardUpdateEntity> {
    List<BoardUpdateEntity> findByBoard_SerialAndVersionAndTypeOrderByReleaseDateAsc(String serial, String version, UpdateType type);

    List<BoardUpdateEntity> findByBoard_SerialAndBoard_VersionAndVersionAndTypeOrderByReleaseDateAsc(String serial, String version, String version1, UpdateType type);
}
