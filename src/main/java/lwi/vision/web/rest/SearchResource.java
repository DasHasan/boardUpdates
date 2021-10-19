package lwi.vision.web.rest;

import lwi.vision.domain.SearchUpdateRequest;
import lwi.vision.domain.SearchUpdateResponse;
import lwi.vision.domain.enumeration.UpdateType;
import lwi.vision.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SearchResource controller
 */
@RestController
@RequestMapping("/search")
public class SearchResource {

    private final Logger log = LoggerFactory.getLogger(SearchResource.class);
    private final SearchService searchService;

    public SearchResource(SearchService searchService) {
        this.searchService = searchService;
    }

    // anzahl update speichern

    // localhost:8080/search/software
    // {
    //      serial: 'asdf',
    //      firmware: '1.0',
    //      software: '1.0',
    //      updateKeys: [..., ...]
    // }

    // {
    //    software: {
    //        version: '2.0',
    //        mandatory: false|true,
    //        path: '...',
    //        updateKeys: [..., ...]
    //    }
    // }
    @PostMapping(value = "/software", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SearchUpdateResponse> searchSoftware(@RequestBody SearchUpdateRequest request) {
        return ResponseEntity.ok().body(searchService.search(request, UpdateType.SOFTWARE));
    }

    @PostMapping(value = "/firmware", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SearchUpdateResponse> searchFirmware(@RequestBody SearchUpdateRequest request) {
        return ResponseEntity.ok().body(searchService.search(request, UpdateType.FIRMWARE));
    }
}
