package lwi.vision.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import lwi.vision.domain.*; // for static metamodels
import lwi.vision.domain.GroupEntity;
import lwi.vision.repository.GroupRepository;
import lwi.vision.service.criteria.GroupCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link GroupEntity} entities in the database.
 * The main input is a {@link GroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GroupEntity} or a {@link Page} of {@link GroupEntity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GroupQueryService extends QueryService<GroupEntity> {

    private final Logger log = LoggerFactory.getLogger(GroupQueryService.class);

    private final GroupRepository groupRepository;

    public GroupQueryService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /**
     * Return a {@link List} of {@link GroupEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GroupEntity> findByCriteria(GroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GroupEntity> specification = createSpecification(criteria);
        return groupRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GroupEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GroupEntity> findByCriteria(GroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GroupEntity> specification = createSpecification(criteria);
        return groupRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GroupEntity> specification = createSpecification(criteria);
        return groupRepository.count(specification);
    }

    /**
     * Function to convert {@link GroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GroupEntity> createSpecification(GroupCriteria criteria) {
        Specification<GroupEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GroupEntity_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), GroupEntity_.name));
            }
        }
        return specification;
    }
}
