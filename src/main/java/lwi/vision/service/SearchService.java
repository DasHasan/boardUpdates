package lwi.vision.service;

import java.util.HashMap;
import java.util.Optional;
import lwi.vision.domain.*;
import lwi.vision.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SearchService {

    private final Logger log = LoggerFactory.getLogger(SearchService.class);
    private final ExtendedBoardRepository boardRepository;
    private final ExtendedSoftwareUpdateRepository softwareUpdateRepository;
    private final ExtendedSoftwareRepository softwareRepository;
    private final ExtendedFirmwareRepository firmwareRepository;
    private final ExtendedFirmwareUpdateRepository firmwareUpdateRepository;

    public SearchService(
        ExtendedBoardRepository boardRepository,
        ExtendedSoftwareUpdateRepository softwareUpdateRepository,
        ExtendedSoftwareRepository softwareRepository,
        ExtendedFirmwareRepository firmwareRepository,
        ExtendedFirmwareUpdateRepository firmwareUpdateRepository
    ) {
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
}
