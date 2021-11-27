package lwi.vision.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import lwi.vision.domain.*; // for static metamodels
import lwi.vision.domain.DownloadEntity;
import lwi.vision.repository.DownloadRepository;
import lwi.vision.service.criteria.DownloadCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DownloadEntity} entities in the database.
 * The main input is a {@link DownloadCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DownloadEntity} or a {@link Page} of {@link DownloadEntity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DownloadQueryService extends QueryService<DownloadEntity> {

    private final Logger log = LoggerFactory.getLogger(DownloadQueryService.class);

    private final DownloadRepository downloadRepository;

    public DownloadQueryService(DownloadRepository downloadRepository) {
        this.downloadRepository = downloadRepository;
    }

    /**
     * Return a {@link List} of {@link DownloadEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DownloadEntity> findByCriteria(DownloadCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DownloadEntity> specification = createSpecification(criteria);
        return downloadRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DownloadEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DownloadEntity> findByCriteria(DownloadCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DownloadEntity> specification = createSpecification(criteria);
        return downloadRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DownloadCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DownloadEntity> specification = createSpecification(criteria);
        return downloadRepository.count(specification);
    }

    /**
     * Function to convert {@link DownloadCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DownloadEntity> createSpecification(DownloadCriteria criteria) {
        Specification<DownloadEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DownloadEntity_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), DownloadEntity_.date));
            }
            if (criteria.getBoardUpdateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBoardUpdateId(),
                            root -> root.join(DownloadEntity_.boardUpdate, JoinType.LEFT).get(BoardUpdateEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
