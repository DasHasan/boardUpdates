package lwi.vision.service;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.StringPath;
import lwi.vision.domain.*;
import lwi.vision.domain.enumeration.UpdateType;
import lwi.vision.repository.BoardUpdateRepository;
import lwi.vision.repository.BoardUpdateSuccessorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
public class SearchService {

    private final Logger log = LoggerFactory.getLogger(SearchService.class);
    private final BoardUpdateRepository boardUpdateRepository;

    private final DownloadUrlService downloadUrlService;

    public SearchService(
        BoardUpdateRepository boardUpdateRepository,
        DownloadUrlService downloadUrlService,
        BoardUpdateSuccessorRepository boardUpdateSuccessorRepository
    ) {
        this.boardUpdateRepository = boardUpdateRepository;
        this.downloadUrlService = downloadUrlService;
    }

    public SearchUpdateResponse search(SearchUpdateRequest request, UpdateType updateType) {
        String version = updateType == UpdateType.SOFTWARE ? request.getSoftware() : request.getFirmware();
        return getUpdateResponse(request, updateType, version);
    }

    private SearchUpdateResponse getUpdateResponse(SearchUpdateRequest request, UpdateType type, String version) {
        List<BoardUpdateEntity> siblingUpdates = findAllSiblingUpdates(request, type, version);

        Optional<BoardUpdateEntity> filteredUpdates = siblingUpdates.stream()
            .filter(byUpdateKeys(request))
            .findFirst();

        return siblingUpdates.stream().map(this::toSearchUpdateResponse).findFirst().orElse(new SearchUpdateResponse());

        // angefragtes Update konnte nicht gefunden werden
//        if (filteredUpdates.isEmpty()) {
//            log.error("Kein Update gefunden für Anfrage: " + request);
//            return new SearchUpdateResponse();
//        }
//
//        Optional<BoardUpdateSuccessorEntity> updateSuccessor = boardUpdateSuccessorRepository.findFirstByFrom_Id(
//            filteredUpdates.get().getId()
//        );
//        // angefragtes update hat keinen Nachfolger definiert
//        if (updateSuccessor.isEmpty()) {
//            log.error("Kein Nachfolger definiert für Update: " + filteredUpdates.get());
//            return new SearchUpdateResponse();
//        }
//
//        BoardUpdateEntity update = updateSuccessor.get().getTo();
//        return toSearchUpdateResponse(update);
    }

    private List<BoardUpdateEntity> findAllSiblingUpdates(SearchUpdateRequest request, UpdateType type, String version) {
        StringPath boardVersion = QBoardUpdateEntity.boardUpdateEntity.board.version;
        StringPath boardSerial = QBoardUpdateEntity.boardUpdateEntity.board.serial;
        EnumPath<UpdateType> updateType = QBoardUpdateEntity.boardUpdateEntity.type;
        StringPath updateVersion = QBoardUpdateEntity.boardUpdateEntity.version;
        OrderSpecifier<ZonedDateTime> orderByReleaseDate = QBoardUpdateEntity.boardUpdateEntity.releaseDate.desc();
        BooleanExpression isReleased = QBoardUpdateEntity.boardUpdateEntity.releaseDate.before(ZonedDateTime.now());
        StringPath updateStatus = QBoardUpdateEntity.boardUpdateEntity.status;
        Page<BoardUpdateEntity> updateEntities = boardUpdateRepository.findAll(
            boardSerial.eq(request.getSerial())
                .and(boardVersion.eq(request.getVersion()))
                .and(updateType.eq(type))
                .and(updateKeysEq(request.getUpdateKeys()))
                .and(updateStatus.eq(request.getStatus()))
                .and(isReleased)
                //.and(updateVersion.eq(version))
            , QPageRequest.of(0, 10, orderByReleaseDate));

        return updateEntities.toList();

//        return boardUpdateRepository.findByBoard_SerialAndBoard_VersionAndVersionAndTypeOrderByReleaseDateAsc(
//            request.getSerial(),
//            request.getVersion(),
//            version,
//            type
//        );
    }

    private BooleanExpression updateKeysEq(List<String> updateKeys) {
        BooleanExpression expression = QBoardUpdateEntity.boardUpdateEntity.updateKeys.size().eq(updateKeys.size());
        for (String updateKey : updateKeys) {
            expression = expression.and(QUpdateKeysEntity.updateKeysEntity.key.eq(updateKey));
        }
        return expression;
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
