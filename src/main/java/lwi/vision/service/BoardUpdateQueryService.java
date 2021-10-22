package lwi.vision.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import lwi.vision.domain.*; // for static metamodels
import lwi.vision.domain.BoardUpdateEntity;
import lwi.vision.repository.BoardUpdateRepository;
import lwi.vision.service.criteria.BoardUpdateCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link BoardUpdateEntity} entities in the database.
 * The main input is a {@link BoardUpdateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BoardUpdateEntity} or a {@link Page} of {@link BoardUpdateEntity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BoardUpdateQueryService extends QueryService<BoardUpdateEntity> {

    private final Logger log = LoggerFactory.getLogger(BoardUpdateQueryService.class);

    private final BoardUpdateRepository boardUpdateRepository;

    public BoardUpdateQueryService(BoardUpdateRepository boardUpdateRepository) {
        this.boardUpdateRepository = boardUpdateRepository;
    }

    /**
     * Return a {@link List} of {@link BoardUpdateEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BoardUpdateEntity> findByCriteria(BoardUpdateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BoardUpdateEntity> specification = createSpecification(criteria);
        return boardUpdateRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BoardUpdateEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BoardUpdateEntity> findByCriteria(BoardUpdateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BoardUpdateEntity> specification = createSpecification(criteria);
        return boardUpdateRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BoardUpdateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BoardUpdateEntity> specification = createSpecification(criteria);
        return boardUpdateRepository.count(specification);
    }

    /**
     * Function to convert {@link BoardUpdateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BoardUpdateEntity> createSpecification(BoardUpdateCriteria criteria) {
        Specification<BoardUpdateEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BoardUpdateEntity_.id));
            }
            if (criteria.getVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVersion(), BoardUpdateEntity_.version));
            }
            if (criteria.getPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPath(), BoardUpdateEntity_.path));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), BoardUpdateEntity_.type));
            }
            if (criteria.getReleaseDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReleaseDate(), BoardUpdateEntity_.releaseDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), BoardUpdateEntity_.status));
            }
            if (criteria.getUpdateKeysId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUpdateKeysId(),
                            root -> root.join(BoardUpdateEntity_.updateKeys, JoinType.LEFT).get(UpdateKeysEntity_.id)
                        )
                    );
            }
            if (criteria.getBoardId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBoardId(),
                            root -> root.join(BoardUpdateEntity_.board, JoinType.LEFT).get(BoardEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
