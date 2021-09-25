package lwi.vision.service;

import lwi.vision.domain.Board;
import lwi.vision.domain.SoftwareUpdate;
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

    public SearchService(MyBoardRepository boardRepository, ExtendedSoftwareUpdateRepository softwareUpdateRepository) {
        this.boardRepository = boardRepository;
        this.softwareUpdateRepository = softwareUpdateRepository;
    }

    public String search(String serial, String firmware, String softwareVersion) {
        SoftwareUpdate softwareUpdate = softwareUpdateRepository.findByBoard_SerialAndFrom_Version(serial, softwareVersion).orElseThrow();
        log.info(softwareUpdate.getBoard().toString());
        log.info(softwareUpdate.getTo().getPath());
        Board board = boardRepository.findBySerialIs(serial).orElseThrow();

        return "";
    }
}
