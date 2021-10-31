package lwi.vision.service;

import lwi.vision.domain.BoardUpdateEntity;
import lwi.vision.web.rest.errors.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DownloadService {

    private final Logger log = LoggerFactory.getLogger(DownloadService.class);
    private final BoardUpdateService boardUpdateService;

    public DownloadService(BoardUpdateService boardUpdateService) {
        this.boardUpdateService = boardUpdateService;
    }

    public String getDownload(Long id) {
        return boardUpdateService.findOne(id).orElseThrow(ServiceException::new).getPath();
    }
}
