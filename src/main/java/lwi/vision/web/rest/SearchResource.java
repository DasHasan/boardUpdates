package lwi.vision.web.rest;

import java.util.Map;
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

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> search(@RequestBody Map<String, String> request) {
        String serial = request.get("serial");
        String firmware = request.get("firmware");
        String software = request.get("software");
        return ResponseEntity.ok().body(searchService.search(serial, firmware, software));
    }
}
