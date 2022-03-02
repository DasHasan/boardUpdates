package lwi.vision.repository;

import lwi.vision.domain.BoardEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BoardEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long>, JpaSpecificationExecutor<BoardEntity>, QuerydslPredicateExecutor<BoardRepository> {}
