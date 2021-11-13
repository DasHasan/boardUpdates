package lwi.vision.service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lwi.vision.domain.BoardUpdateEntity;
import lwi.vision.domain.SearchUpdateRequest;
import lwi.vision.domain.SearchUpdateResponse;
import lwi.vision.domain.UpdateKeysEntity;
import lwi.vision.domain.enumeration.UpdateType;
import lwi.vision.repository.BoardUpdateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SearchService {

    private final Logger log = LoggerFactory.getLogger(SearchService.class);
    private final BoardUpdateRepository boardUpdateRepository;

    public SearchService(BoardUpdateRepository boardUpdateRepository) {
        this.boardUpdateRepository = boardUpdateRepository;
    }

    public SearchUpdateResponse search(SearchUpdateRequest request, UpdateType updateType) {
        String version = request.getSoftware();
        return getUpdateResponse(request, updateType, version);
    }

    private SearchUpdateResponse getUpdateResponse(SearchUpdateRequest request, UpdateType type, String version) {
        List<BoardUpdateEntity> updateEntities = getUpdateEntities(request, type, version);

        List<SearchUpdateResponse> responseList = buildUpdateResponse(request, updateEntities);

        return responseList.size() > 0 ? responseList.get(0) : new SearchUpdateResponse();
    }

    private List<SearchUpdateResponse> buildUpdateResponse(SearchUpdateRequest request, List<BoardUpdateEntity> updateEntities) {
        return updateEntities
            .stream()
            .map(
                boardUpdateEntity -> {
                    SearchUpdateResponse response = new SearchUpdateResponse();
                    response.setVersion(boardUpdateEntity.getVersion());
                    response.setPath(boardUpdateEntity.getPath());
                    response.setMandatory("false");
                    response.getUpdateKeys().addAll(buildUpdateKeys(boardUpdateEntity));
                    return response;
                }
            )
            .filter(filterByUpdateKeys(request))
            .collect(Collectors.toList());
    }

    private Predicate<SearchUpdateResponse> filterByUpdateKeys(SearchUpdateRequest request) {
        return searchUpdateResponse -> searchUpdateResponse.getUpdateKeys().containsAll(request.getUpdateKeys());
    }

    private List<String> buildUpdateKeys(BoardUpdateEntity boardUpdateEntity) {
        return boardUpdateEntity.getUpdateKeys().stream().map(UpdateKeysEntity::getKey).collect(Collectors.toList());
    }

    private List<BoardUpdateEntity> getUpdateEntities(SearchUpdateRequest request, UpdateType type, String version) {
        return boardUpdateRepository.findByBoard_SerialAndVersionAndTypeOrderByReleaseDateAsc(request.getSerial(), version, type);
    }
}
