package lwi.vision.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lwi.vision.domain.DownloadEntity;
import lwi.vision.repository.DownloadRepository;
import lwi.vision.service.DownloadQueryService;
import lwi.vision.service.DownloadService;
import lwi.vision.service.criteria.DownloadCriteria;
import lwi.vision.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link lwi.vision.domain.DownloadEntity}.
 */
@RestController
@RequestMapping("/api")
public class DownloadResource {

    private final Logger log = LoggerFactory.getLogger(DownloadResource.class);

    private static final String ENTITY_NAME = "download";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DownloadService downloadService;

    private final DownloadRepository downloadRepository;

    private final DownloadQueryService downloadQueryService;

    public DownloadResource(
        DownloadService downloadService,
        DownloadRepository downloadRepository,
        DownloadQueryService downloadQueryService
    ) {
        this.downloadService = downloadService;
        this.downloadRepository = downloadRepository;
        this.downloadQueryService = downloadQueryService;
    }

    /**
     * {@code POST  /downloads} : Create a new download.
     *
     * @param downloadEntity the downloadEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new downloadEntity, or with status {@code 400 (Bad Request)} if the download has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/downloads")
    public ResponseEntity<DownloadEntity> createDownload(@RequestBody DownloadEntity downloadEntity) throws URISyntaxException {
        log.debug("REST request to save Download : {}", downloadEntity);
        if (downloadEntity.getId() != null) {
            throw new BadRequestAlertException("A new download cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DownloadEntity result = downloadService.save(downloadEntity);
        return ResponseEntity
            .created(new URI("/api/downloads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /downloads/:id} : Updates an existing download.
     *
     * @param id the id of the downloadEntity to save.
     * @param downloadEntity the downloadEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated downloadEntity,
     * or with status {@code 400 (Bad Request)} if the downloadEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the downloadEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/downloads/{id}")
    public ResponseEntity<DownloadEntity> updateDownload(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DownloadEntity downloadEntity
    ) throws URISyntaxException {
        log.debug("REST request to update Download : {}, {}", id, downloadEntity);
        if (downloadEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, downloadEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!downloadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DownloadEntity result = downloadService.save(downloadEntity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, downloadEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /downloads/:id} : Partial updates given fields of an existing download, field will ignore if it is null
     *
     * @param id the id of the downloadEntity to save.
     * @param downloadEntity the downloadEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated downloadEntity,
     * or with status {@code 400 (Bad Request)} if the downloadEntity is not valid,
     * or with status {@code 404 (Not Found)} if the downloadEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the downloadEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/downloads/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DownloadEntity> partialUpdateDownload(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DownloadEntity downloadEntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update Download partially : {}, {}", id, downloadEntity);
        if (downloadEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, downloadEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!downloadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DownloadEntity> result = downloadService.partialUpdate(downloadEntity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, downloadEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /downloads} : get all the downloads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of downloads in body.
     */
    @GetMapping("/downloads")
    public ResponseEntity<List<DownloadEntity>> getAllDownloads(DownloadCriteria criteria) {
        log.debug("REST request to get Downloads by criteria: {}", criteria);
        List<DownloadEntity> entityList = downloadQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /downloads/count} : count all the downloads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/downloads/count")
    public ResponseEntity<Long> countDownloads(DownloadCriteria criteria) {
        log.debug("REST request to count Downloads by criteria: {}", criteria);
        return ResponseEntity.ok().body(downloadQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /downloads/:id} : get the "id" download.
     *
     * @param id the id of the downloadEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the downloadEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/downloads/{id}")
    public ResponseEntity<DownloadEntity> getDownload(@PathVariable Long id) {
        log.debug("REST request to get Download : {}", id);
        Optional<DownloadEntity> downloadEntity = downloadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(downloadEntity);
    }

    /**
     * {@code DELETE  /downloads/:id} : delete the "id" download.
     *
     * @param id the id of the downloadEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/downloads/{id}")
    public ResponseEntity<Void> deleteDownload(@PathVariable Long id) {
        log.debug("REST request to delete Download : {}", id);
        downloadService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
