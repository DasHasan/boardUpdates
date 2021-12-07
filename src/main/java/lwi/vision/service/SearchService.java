package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lwi.vision.domain.*;
import lwi.vision.domain.enumeration.UpdateType;
import lwi.vision.repository.BoardUpdateRepository;
import lwi.vision.repository.BoardUpdateSuccessorRepository;
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

    private final BoardUpdateSuccessorRepository boardUpdateSuccessorRepository;

    public SearchService(
        BoardUpdateRepository boardUpdateRepository,
        DownloadUrlService downloadUrlService,
        BoardUpdateSuccessorRepository boardUpdateSuccessorRepository
    ) {
        this.boardUpdateRepository = boardUpdateRepository;
        this.downloadUrlService = downloadUrlService;
        this.boardUpdateSuccessorRepository = boardUpdateSuccessorRepository;
    }

    public SearchUpdateResponse search(SearchUpdateRequest request, UpdateType updateType) {
        String version = updateType == UpdateType.SOFTWARE ? request.getSoftware() : request.getFirmware();
        return getUpdateResponse(request, updateType, version);
    }

    private SearchUpdateResponse getUpdateResponse(SearchUpdateRequest request, UpdateType type, String version) {
        List<BoardUpdateEntity> updateEntities = boardUpdateRepository.findByBoard_SerialAndVersionAndTypeOrderByReleaseDateAsc(
            request.getSerial(),
            version,
            type
        );

        Optional<BoardUpdateEntity> foundUpdateEntity = updateEntities.stream().filter(byUpdateKeys(request)).findFirst();

        // angefragtes Update konnte nicht gefunden werden
        if (foundUpdateEntity.isEmpty()) {
            log.error("Kein Update gefunden für Anfrage: " + request);
            return new SearchUpdateResponse();
        }

        Optional<BoardUpdateSuccessorEntity> updateSuccessor = boardUpdateSuccessorRepository.findFirstByFrom_Id(
            foundUpdateEntity.get().getId()
        );
        // angefragtes update hat keinen Nachfolger definiert
        if (updateSuccessor.isEmpty()) {
            log.error("Kein Nachfolger definiert für Update: " + foundUpdateEntity.get());
            return new SearchUpdateResponse();
        }

        BoardUpdateEntity update = updateSuccessor.get().getTo();
        return toSearchUpdateResponse(update);
    }

    private SearchUpdateResponse toSearchUpdateResponse(BoardUpdateEntity update) {
        SearchUpdateResponse response = new SearchUpdateResponse();
        response.setVersion(update.getVersion());
        response.setMandatory("false");
        response.setUpdateKeys(buildUpdateKeys(update));
        response.setDownloadUrl(getDownloadUrl(update));
        response.setStatus(update.getStatus());
        response.setUpdateType(update.getType());
        return response;
    }

    private String getDownloadUrl(BoardUpdateEntity boardUpdateEntity) {
        DownloadUrlEntity entity = downloadUrlService.getOrCreateByBoardUpdate(boardUpdateEntity);
        return "/download/" + entity.getUrl();
    }

    private List<String> buildUpdateKeys(BoardUpdateEntity boardUpdateEntity) {
        return boardUpdateEntity.getUpdateKeys().stream().map(UpdateKeysEntity::getKey).collect(Collectors.toList());
    }

    private Predicate<BoardUpdateEntity> byUpdateKeys(SearchUpdateRequest request) {
        return updateEntity -> {
            List<String> updateKeys = updateEntity.getUpdateKeys().stream().map(UpdateKeysEntity::getKey).collect(Collectors.toList());
            return (
                updateKeys.size() == request.getUpdateKeys().size() && // equal size
                updateKeys.containsAll(request.getUpdateKeys())
            ); // equal content
        };
    }
}
