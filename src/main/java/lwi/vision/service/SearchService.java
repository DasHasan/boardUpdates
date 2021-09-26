package lwi.vision.service;

import java.util.HashMap;
import java.util.Optional;
import lwi.vision.domain.Firmware;
import lwi.vision.domain.Software;
import lwi.vision.domain.SoftwareUpdate;
import lwi.vision.repository.ExtendedFirmwareRepository;
import lwi.vision.repository.ExtendedSoftwareRepository;
import lwi.vision.repository.ExtendedSoftwareUpdateRepository;
import lwi.vision.repository.MyBoardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SearchService {

    private final Logger log = LoggerFactory.getLogger(SearchService.class);
    private final MyBoardRepository boardRepository;
    private final ExtendedSoftwareUpdateRepository softwareUpdateRepository;
    private final ExtendedSoftwareRepository softwareRepository;
    private final ExtendedFirmwareRepository firmwareRepository;

    public SearchService(
        MyBoardRepository boardRepository,
        ExtendedSoftwareUpdateRepository softwareUpdateRepository,
        ExtendedSoftwareRepository softwareRepository,
        ExtendedFirmwareRepository firmwareRepository
    ) {
        this.boardRepository = boardRepository;
        this.softwareUpdateRepository = softwareUpdateRepository;
        this.softwareRepository = softwareRepository;
        this.firmwareRepository = firmwareRepository;
    }

    public HashMap<String, String> search(String serial, String firmwareVersion, String softwareVersion) {
        Software software = new Software().version("false").path("");
        Firmware firmware = new Firmware().version("false").path("");

        Optional<SoftwareUpdate> softwareUpdateOptional = softwareUpdateRepository.findByBoard_SerialAndFrom_Version(
            serial,
            softwareVersion
        );
        if (softwareUpdateOptional.isPresent()) {
            Software to = softwareUpdateOptional.get().getTo();
            if (to == null) {
                software = to;
            }
        } else {
            software = softwareRepository.findFirstByBoard_SerialIsOrderByCreatedDateAsc(serial).orElse(software);
            firmware = firmwareRepository.findFirstByBoard_SerialIsOrderByCreatedDateAsc(serial).orElse(firmware);
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("sw_update", software.getVersion());
        map.put("fw_update", firmware.getVersion());
        map.put("sw_path", software.getPath());
        map.put("fw_path", firmware.getPath());
        return map;
    }
}
