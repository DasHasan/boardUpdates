package lwi.vision.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import lwi.vision.domain.DownloadUrlEntity;
import lwi.vision.repository.DownloadUrlRepository;
import lwi.vision.service.DownloadUrlQueryService;
import lwi.vision.service.DownloadUrlService;
import lwi.vision.service.criteria.DownloadUrlCriteria;
import lwi.vision.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link lwi.vision.domain.DownloadUrlEntity}.
 */
@RestController
@RequestMapping("/api")
public class DownloadUrlResource {

    private final Logger log = LoggerFactory.getLogger(DownloadUrlResource.class);

    private static final String ENTITY_NAME = "downloadUrl";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DownloadUrlService downloadUrlService;

    private final DownloadUrlRepository downloadUrlRepository;

    private final DownloadUrlQueryService downloadUrlQueryService;

    public DownloadUrlResource(
        DownloadUrlService downloadUrlService,
        DownloadUrlRepository downloadUrlRepository,
        DownloadUrlQueryService downloadUrlQueryService
    ) {
        this.downloadUrlService = downloadUrlService;
        this.downloadUrlRepository = downloadUrlRepository;
        this.downloadUrlQueryService = downloadUrlQueryService;
    }

    /**
     * {@code POST  /download-urls} : Create a new downloadUrl.
     *
     * @param downloadUrlEntity the downloadUrlEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new downloadUrlEntity, or with status {@code 400 (Bad Request)} if the downloadUrl has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/download-urls")
    public ResponseEntity<DownloadUrlEntity> createDownloadUrl(@RequestBody DownloadUrlEntity downloadUrlEntity) throws URISyntaxException {
        log.debug("REST request to save DownloadUrl : {}", downloadUrlEntity);
        if (downloadUrlEntity.getId() != null) {
            throw new BadRequestAlertException("A new downloadUrl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DownloadUrlEntity result = downloadUrlService.save(downloadUrlEntity);
        return ResponseEntity
            .created(new URI("/api/download-urls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /download-urls/:id} : Updates an existing downloadUrl.
     *
     * @param id the id of the downloadUrlEntity to save.
     * @param downloadUrlEntity the downloadUrlEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated downloadUrlEntity,
     * or with status {@code 400 (Bad Request)} if the downloadUrlEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the downloadUrlEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/download-urls/{id}")
    public ResponseEntity<DownloadUrlEntity> updateDownloadUrl(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DownloadUrlEntity downloadUrlEntity
    ) throws URISyntaxException {
        log.debug("REST request to update DownloadUrl : {}, {}", id, downloadUrlEntity);
        if (downloadUrlEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, downloadUrlEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!downloadUrlRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DownloadUrlEntity result = downloadUrlService.save(downloadUrlEntity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, downloadUrlEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /download-urls/:id} : Partial updates given fields of an existing downloadUrl, field will ignore if it is null
     *
     * @param id the id of the downloadUrlEntity to save.
     * @param downloadUrlEntity the downloadUrlEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated downloadUrlEntity,
     * or with status {@code 400 (Bad Request)} if the downloadUrlEntity is not valid,
     * or with status {@code 404 (Not Found)} if the downloadUrlEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the downloadUrlEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/download-urls/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DownloadUrlEntity> partialUpdateDownloadUrl(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DownloadUrlEntity downloadUrlEntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update DownloadUrl partially : {}, {}", id, downloadUrlEntity);
        if (downloadUrlEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, downloadUrlEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!downloadUrlRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DownloadUrlEntity> result = downloadUrlService.partialUpdate(downloadUrlEntity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, downloadUrlEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /download-urls} : get all the downloadUrls.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of downloadUrls in body.
     */
    @GetMapping("/download-urls")
    public ResponseEntity<List<DownloadUrlEntity>> getAllDownloadUrls(DownloadUrlCriteria criteria) {
        log.debug("REST request to get DownloadUrls by criteria: {}", criteria);
        List<DownloadUrlEntity> entityList = downloadUrlQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /download-urls/count} : count all the downloadUrls.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/download-urls/count")
    public ResponseEntity<Long> countDownloadUrls(DownloadUrlCriteria criteria) {
        log.debug("REST request to count DownloadUrls by criteria: {}", criteria);
        return ResponseEntity.ok().body(downloadUrlQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /download-urls/:id} : get the "id" downloadUrl.
     *
     * @param id the id of the downloadUrlEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the downloadUrlEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/download-urls/{id}")
    public ResponseEntity<DownloadUrlEntity> getDownloadUrl(@PathVariable Long id) {
        log.debug("REST request to get DownloadUrl : {}", id);
        Optional<DownloadUrlEntity> downloadUrlEntity = downloadUrlService.findOne(id);
        return ResponseUtil.wrapOrNotFound(downloadUrlEntity);
    }

    /**
     * {@code DELETE  /download-urls/:id} : delete the "id" downloadUrl.
     *
     * @param id the id of the downloadUrlEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/download-urls/{id}")
    public ResponseEntity<Void> deleteDownloadUrl(@PathVariable Long id) {
        log.debug("REST request to delete DownloadUrl : {}", id);
        downloadUrlService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
