package lwi.vision.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import lwi.vision.domain.*; // for static metamodels
import lwi.vision.domain.DownloadUrlEntity;
import lwi.vision.repository.DownloadUrlRepository;
import lwi.vision.service.criteria.DownloadUrlCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DownloadUrlEntity} entities in the database.
 * The main input is a {@link DownloadUrlCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DownloadUrlEntity} or a {@link Page} of {@link DownloadUrlEntity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DownloadUrlQueryService extends QueryService<DownloadUrlEntity> {

    private final Logger log = LoggerFactory.getLogger(DownloadUrlQueryService.class);

    private final DownloadUrlRepository downloadUrlRepository;

    public DownloadUrlQueryService(DownloadUrlRepository downloadUrlRepository) {
        this.downloadUrlRepository = downloadUrlRepository;
    }

    /**
     * Return a {@link List} of {@link DownloadUrlEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DownloadUrlEntity> findByCriteria(DownloadUrlCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DownloadUrlEntity> specification = createSpecification(criteria);
        return downloadUrlRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DownloadUrlEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DownloadUrlEntity> findByCriteria(DownloadUrlCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DownloadUrlEntity> specification = createSpecification(criteria);
        return downloadUrlRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DownloadUrlCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DownloadUrlEntity> specification = createSpecification(criteria);
        return downloadUrlRepository.count(specification);
    }

    /**
     * Function to convert {@link DownloadUrlCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DownloadUrlEntity> createSpecification(DownloadUrlCriteria criteria) {
        Specification<DownloadUrlEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DownloadUrlEntity_.id));
            }
            if (criteria.getExpirationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpirationDate(), DownloadUrlEntity_.expirationDate));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), DownloadUrlEntity_.url));
            }
            if (criteria.getBoardUpdateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBoardUpdateId(),
                            root -> root.join(DownloadUrlEntity_.boardUpdate, JoinType.LEFT).get(BoardUpdateEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
