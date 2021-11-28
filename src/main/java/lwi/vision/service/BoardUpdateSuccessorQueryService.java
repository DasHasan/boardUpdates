package lwi.vision.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import lwi.vision.domain.*; // for static metamodels
import lwi.vision.domain.BoardUpdateSuccessorEntity;
import lwi.vision.repository.BoardUpdateSuccessorRepository;
import lwi.vision.service.criteria.BoardUpdateSuccessorCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link BoardUpdateSuccessorEntity} entities in the database.
 * The main input is a {@link BoardUpdateSuccessorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BoardUpdateSuccessorEntity} or a {@link Page} of {@link BoardUpdateSuccessorEntity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BoardUpdateSuccessorQueryService extends QueryService<BoardUpdateSuccessorEntity> {

    private final Logger log = LoggerFactory.getLogger(BoardUpdateSuccessorQueryService.class);

    private final BoardUpdateSuccessorRepository boardUpdateSuccessorRepository;

    public BoardUpdateSuccessorQueryService(BoardUpdateSuccessorRepository boardUpdateSuccessorRepository) {
        this.boardUpdateSuccessorRepository = boardUpdateSuccessorRepository;
    }

    /**
     * Return a {@link List} of {@link BoardUpdateSuccessorEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BoardUpdateSuccessorEntity> findByCriteria(BoardUpdateSuccessorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BoardUpdateSuccessorEntity> specification = createSpecification(criteria);
        return boardUpdateSuccessorRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BoardUpdateSuccessorEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BoardUpdateSuccessorEntity> findByCriteria(BoardUpdateSuccessorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BoardUpdateSuccessorEntity> specification = createSpecification(criteria);
        return boardUpdateSuccessorRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BoardUpdateSuccessorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BoardUpdateSuccessorEntity> specification = createSpecification(criteria);
        return boardUpdateSuccessorRepository.count(specification);
    }

    /**
     * Function to convert {@link BoardUpdateSuccessorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BoardUpdateSuccessorEntity> createSpecification(BoardUpdateSuccessorCriteria criteria) {
        Specification<BoardUpdateSuccessorEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BoardUpdateSuccessorEntity_.id));
            }
            if (criteria.getFromId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFromId(),
                            root -> root.join(BoardUpdateSuccessorEntity_.from, JoinType.LEFT).get(BoardUpdateEntity_.id)
                        )
                    );
            }
            if (criteria.getToId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getToId(),
                            root -> root.join(BoardUpdateSuccessorEntity_.to, JoinType.LEFT).get(BoardUpdateEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
