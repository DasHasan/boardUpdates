package lwi.vision.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lwi.vision.domain.*;
import lwi.vision.domain.enumeration.UpdateType;
import lwi.vision.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SearchService {

    private final Logger log = LoggerFactory.getLogger(SearchService.class);
    private final ExtendedSoftwareUpdateRepository softwareUpdateRepository;
    private final ExtendedSoftwareRepository softwareRepository;
    private final ExtendedFirmwareRepository firmwareRepository;
    private final ExtendedFirmwareUpdateRepository firmwareUpdateRepository;
    private final BoardUpdateRepository boardUpdateRepository;

    public SearchService(
        ExtendedSoftwareUpdateRepository softwareUpdateRepository,
        ExtendedSoftwareRepository softwareRepository,
        ExtendedFirmwareRepository firmwareRepository,
        ExtendedFirmwareUpdateRepository firmwareUpdateRepository,
        BoardUpdateRepository boardUpdateRepository
    ) {
        this.softwareUpdateRepository = softwareUpdateRepository;
        this.softwareRepository = softwareRepository;
        this.firmwareRepository = firmwareRepository;
        this.firmwareUpdateRepository = firmwareUpdateRepository;
        this.boardUpdateRepository = boardUpdateRepository;
    }

    public HashMap<String, String> search(String serial, String firmwareVersion, String softwareVersion) {
        SoftwareEntity defaultSoftware = new SoftwareEntity().version("false").path("");
        SoftwareEntity software = defaultSoftware;
        FirmwareEntity defaultFirmware = new FirmwareEntity().version("false").path("");
        FirmwareEntity firmware = defaultFirmware;

        Optional<SoftwareUpdateEntity> softwareUpdateOptional = softwareUpdateRepository.findByBoard_SerialAndFrom_Version(
            serial,
            softwareVersion
        );
        if (softwareUpdateOptional.isPresent()) {
            SoftwareEntity to = softwareUpdateOptional.get().getTo();
            if (to != null) {
                log.info("Found update plan: {}", to);
                software = to;
            }
        } else {
            log.info("software entries for serial: {}", serial);
            for (SoftwareEntity entity : softwareRepository.findByBoard_SerialIsOrderByCreatedDateDesc(serial)) {
                log.info(entity.toString());
            }
            Optional<SoftwareEntity> optional = softwareRepository.findFirstByBoard_SerialIsOrderByCreatedDateDesc(serial);
            if (optional.isPresent()) {
                SoftwareEntity entity = optional.get();
                log.info("Found latest software update: {}", entity);
                software = entity;
            }
        }

        Optional<FirmwareUpdateEntity> firmwareUpdateOptional = firmwareUpdateRepository.findByBoard_SerialIsAndFrom_VersionIs(
            serial,
            firmwareVersion
        );
        if (firmwareUpdateOptional.isPresent()) {
            FirmwareEntity to = firmwareUpdateOptional.get().getTo();
            if (to != null) {
                log.info("Found update plan: {}", to);
                firmware = to;
            }
        } else {
            log.info("firmware entries for serial: {}", serial);
            for (FirmwareEntity entity : firmwareRepository.findByBoard_SerialIsOrderByCreatedDateDesc(serial)) {
                log.info("entity: {}, createdDate: {}", entity.toString(), entity.getCreatedDate().toString());
            }
            Optional<FirmwareEntity> optional = firmwareRepository.findFirstByBoard_SerialIsOrderByCreatedDateDesc(serial);
            if (optional.isPresent()) {
                FirmwareEntity entity = optional.get();
                log.info("Found latest firmware update: {}", entity);
                firmware = entity;
            }
        }

        if (software.getVersion().equals(softwareVersion)) {
            log.info("No software update found");
            software = defaultSoftware;
        }
        if (firmware.getVersion().equals(firmwareVersion)) {
            log.info("No firmware update found");
            firmware = defaultFirmware;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("swupdate", software.getVersion());
        map.put("fwupdate", firmware.getVersion());
        map.put("swpath", software.getPath());
        map.put("fwpath", firmware.getPath());
        // CW = continuous wave

        // Updates auch manuell downloaden (USB-Update):
        // - Anfrage-JSON von Anlage hochladen

        // - frei wählbare updateKeys ["cw", "achsen", ".."]
        // - nur updates mit passendem updateKey finden

        // { serial, version, updateKeys } -->
        //
        // {
        //      software: {
        //          status: ['beta', 'release']
        //          version: '...',
        //      }
        //      firmware: {
        //          status: ['beta', 'release']
        //          version: '...',
        //      }
        //  }

        return map;
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
