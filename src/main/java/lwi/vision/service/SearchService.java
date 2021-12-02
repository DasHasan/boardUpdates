package lwi.vision.service;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lwi.vision.domain.BoardUpdateEntity;
import lwi.vision.domain.SearchUpdateRequest;
import lwi.vision.domain.SearchUpdateResponse;
import lwi.vision.domain.UpdateKeysEntity;
import lwi.vision.domain.enumeration.UpdateType;
import lwi.vision.repository.BoardUpdateRepository;
import lwi.vision.service.criteria.DownloadUrlCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SearchService {

    private final Logger log = LoggerFactory.getLogger(SearchService.class);
    private final BoardUpdateRepository boardUpdateRepository;

    private final DownloadUrlService downloadUrlService;

    private final DownloadUrlQueryService downloadUrlQueryService;

    public SearchService(BoardUpdateRepository boardUpdateRepository, DownloadUrlService downloadUrlService, DownloadUrlQueryService downloadUrlQueryService) {
        this.boardUpdateRepository = boardUpdateRepository;
        this.downloadUrlService = downloadUrlService;
        this.downloadUrlQueryService = downloadUrlQueryService;
    }

    public SearchUpdateResponse search(SearchUpdateRequest request, UpdateType updateType) {
        String version = request.getSoftware();
        return getUpdateResponse(request, updateType, version);
    }

    private SearchUpdateResponse getUpdateResponse(SearchUpdateRequest request, UpdateType type, String version) {
        List<BoardUpdateEntity> updateEntities = getUpdateEntities(request, type, version);

        List<SearchUpdateResponse> responseList = updateEntities
            .stream()
            .map(toSearchUpdateResponse())
            .filter(byUpdateKeys(request))
            .collect(Collectors.toList());

        return responseList.size() > 0 ? responseList.get(0) : new SearchUpdateResponse();
    }

    private Function<BoardUpdateEntity, SearchUpdateResponse> toSearchUpdateResponse() {
        return boardUpdateEntity -> {
            SearchUpdateResponse response = new SearchUpdateResponse();
            response.setVersion(boardUpdateEntity.getVersion());
            response.setMandatory("false");
            response.setUpdateKeys(buildUpdateKeys(boardUpdateEntity));
            response.setDownloadUrl(getDownloadUrl(boardUpdateEntity));
            response.setStatus(boardUpdateEntity.getStatus());
            return response;
        };
    }

    private String getDownloadUrl(BoardUpdateEntity boardUpdateEntity) {
        DownloadUrlCriteria criteria = new DownloadUrlCriteria();
        // TODO
        downloadUrlQueryService.findByCriteria(criteria);
        return "/download/" + boardUpdateEntity.getId().toString();
    }

    private Predicate<SearchUpdateResponse> byUpdateKeys(SearchUpdateRequest request) {
        return searchUpdateResponse -> searchUpdateResponse.getUpdateKeys().containsAll(request.getUpdateKeys());
    }

    private List<String> buildUpdateKeys(BoardUpdateEntity boardUpdateEntity) {
        return boardUpdateEntity.getUpdateKeys().stream().map(UpdateKeysEntity::getKey).collect(Collectors.toList());
    }

    private List<BoardUpdateEntity> getUpdateEntities(SearchUpdateRequest request, UpdateType type, String version) {
        return boardUpdateRepository.findByBoard_SerialAndVersionAndTypeOrderByReleaseDateAsc(request.getSerial(), version, type);
    }
}
