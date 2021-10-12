package lwi.vision.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import lwi.vision.domain.*; // for static metamodels
import lwi.vision.domain.UpdateKeysEntity;
import lwi.vision.repository.UpdateKeysRepository;
import lwi.vision.service.criteria.UpdateKeysCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link UpdateKeysEntity} entities in the database.
 * The main input is a {@link UpdateKeysCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UpdateKeysEntity} or a {@link Page} of {@link UpdateKeysEntity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UpdateKeysQueryService extends QueryService<UpdateKeysEntity> {

    private final Logger log = LoggerFactory.getLogger(UpdateKeysQueryService.class);

    private final UpdateKeysRepository updateKeysRepository;

    public UpdateKeysQueryService(UpdateKeysRepository updateKeysRepository) {
        this.updateKeysRepository = updateKeysRepository;
    }

    /**
     * Return a {@link List} of {@link UpdateKeysEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UpdateKeysEntity> findByCriteria(UpdateKeysCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UpdateKeysEntity> specification = createSpecification(criteria);
        return updateKeysRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UpdateKeysEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UpdateKeysEntity> findByCriteria(UpdateKeysCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UpdateKeysEntity> specification = createSpecification(criteria);
        return updateKeysRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UpdateKeysCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UpdateKeysEntity> specification = createSpecification(criteria);
        return updateKeysRepository.count(specification);
    }

    /**
     * Function to convert {@link UpdateKeysCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UpdateKeysEntity> createSpecification(UpdateKeysCriteria criteria) {
        Specification<UpdateKeysEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UpdateKeysEntity_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), UpdateKeysEntity_.key));
            }
            if (criteria.getBoardUpdateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBoardUpdateId(),
                            root -> root.join(UpdateKeysEntity_.boardUpdate, JoinType.LEFT).get(BoardUpdateEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
