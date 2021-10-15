package lwi.vision.web.rest;

import java.util.Map;
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
    public ResponseEntity<Map<String, String>> searchSoftware(@RequestBody Map<String, String> request) {
        String serial = request.get("serial");
        String firmwareVersion = request.get("firmware");
        String softwareVersion = request.get("software");
        String updateKeys = request.get("updateKeys");
        return ResponseEntity.ok().body(searchService.searchNew(serial, UpdateType.SOFTWARE, softwareVersion, firmwareVersion, updateKeys));
    }

    @PostMapping(value = "/firmware", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> searchFirmware(@RequestBody Map<String, String> request) {
        String serial = request.get("serial");
        String firmware = request.get("firmware");
        String software = request.get("software");
        String updateKeys = request.get("updateKeys");
        return ResponseEntity.ok().body(searchService.search(serial, firmware, software));
    }
}
