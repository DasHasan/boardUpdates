package lwi.vision.repository;

import lwi.vision.domain.BoardUpdateSuccessorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the BoardUpdateSuccessorEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoardUpdateSuccessorRepository extends JpaRepository<BoardUpdateSuccessorEntity, Long>, JpaSpecificationExecutor<BoardUpdateSuccessorEntity>, QuerydslPredicateExecutor<BoardUpdateSuccessorEntity> {
    Optional<BoardUpdateSuccessorEntity> findFirstByFrom_Id(Long id);
}
