package lwi.vision.service;

import lwi.vision.domain.*;
import lwi.vision.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

@Service
@Transactional
public class SearchService {

    private final Logger log = LoggerFactory.getLogger(SearchService.class);
    private final MyBoardRepository boardRepository;
    private final ExtendedSoftwareUpdateRepository softwareUpdateRepository;
    private final ExtendedSoftwareRepository softwareRepository;
    private final ExtendedFirmwareRepository firmwareRepository;
    private final ExtendedFirmwareUpdateRepository firmwareUpdateRepository;

    public SearchService(
        MyBoardRepository boardRepository,
        ExtendedSoftwareUpdateRepository softwareUpdateRepository,
        ExtendedSoftwareRepository softwareRepository,
        ExtendedFirmwareRepository firmwareRepository,
        ExtendedFirmwareUpdateRepository firmwareUpdateRepository) {
        this.boardRepository = boardRepository;
        this.softwareUpdateRepository = softwareUpdateRepository;
        this.softwareRepository = softwareRepository;
        this.firmwareRepository = firmwareRepository;
        this.firmwareUpdateRepository = firmwareUpdateRepository;
    }

    public HashMap<String, String> search(String serial, String firmwareVersion, String softwareVersion) {
        SoftwareEntity defaultSoftware = new SoftwareEntity().version("false").path("");
        SoftwareEntity software = defaultSoftware;
        FirmwareEntity defaultFirmware = new FirmwareEntity().version("false").path("");
        FirmwareEntity firmware = defaultFirmware;

        Optional<SoftwareUpdateEntity> softwareUpdateOptional = softwareUpdateRepository.findByBoard_SerialAndFrom_Version(serial, softwareVersion);
        if (softwareUpdateOptional.isPresent()) {
            software = Optional.ofNullable(softwareUpdateOptional.get().getTo()).orElse(software);
        } else {
            software = softwareRepository.findFirstByBoard_SerialIsOrderByCreatedDateDesc(serial).orElse(software);
        }

        Optional<FirmwareUpdateEntity> firmwareUpdateOptional = firmwareUpdateRepository.findByBoard_SerialIsAndFrom_VersionIs(serial, firmwareVersion);
        if (firmwareUpdateOptional.isPresent()) {
            firmware = Optional.ofNullable(firmwareUpdateOptional.get().getTo()).orElse(firmware);
        } else {
            firmware = firmwareRepository.findFirstByBoard_SerialIsOrderByCreatedDateDesc(serial).orElse(firmware);
        }

        if (software.getVersion().equals(softwareVersion)) {
            software = defaultSoftware;
        }
        if (firmware.getVersion().equals(firmwareVersion)) {
            firmware = defaultFirmware;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("sw_update", software.getVersion());
        map.put("fw_update", firmware.getVersion());
        map.put("sw_path", software.getPath());
        map.put("fw_path", firmware.getPath());

        return map;
    }
}
