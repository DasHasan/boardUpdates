package lwi.vision.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import lwi.vision.domain.*; // for static metamodels
import lwi.vision.domain.BoardEntity;
import lwi.vision.repository.BoardRepository;
import lwi.vision.service.criteria.BoardCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link BoardEntity} entities in the database.
 * The main input is a {@link BoardCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BoardEntity} or a {@link Page} of {@link BoardEntity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BoardQueryService extends QueryService<BoardEntity> {

    private final Logger log = LoggerFactory.getLogger(BoardQueryService.class);

    private final BoardRepository boardRepository;

    public BoardQueryService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * Return a {@link List} of {@link BoardEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BoardEntity> findByCriteria(BoardCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BoardEntity> specification = createSpecification(criteria);
        return boardRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BoardEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BoardEntity> findByCriteria(BoardCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BoardEntity> specification = createSpecification(criteria);
        return boardRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BoardCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BoardEntity> specification = createSpecification(criteria);
        return boardRepository.count(specification);
    }

    /**
     * Function to convert {@link BoardCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BoardEntity> createSpecification(BoardCriteria criteria) {
        Specification<BoardEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BoardEntity_.id));
            }
            if (criteria.getSerial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSerial(), BoardEntity_.serial));
            }
            if (criteria.getVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVersion(), BoardEntity_.version));
            }
            if (criteria.getBoardUpdateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBoardUpdateId(),
                            root -> root.join(BoardEntity_.boardUpdates, JoinType.LEFT).get(BoardUpdateEntity_.id)
                        )
                    );
            }
            if (criteria.getUpdatePreconditionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUpdatePreconditionId(),
                            root -> root.join(BoardEntity_.updatePrecondition, JoinType.LEFT).get(UpdatePreconditionEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
