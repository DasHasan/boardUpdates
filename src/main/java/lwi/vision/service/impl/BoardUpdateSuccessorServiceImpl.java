package lwi.vision.service.impl;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.BoardUpdateSuccessorEntity;
import lwi.vision.repository.BoardUpdateSuccessorRepository;
import lwi.vision.service.BoardUpdateSuccessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BoardUpdateSuccessorEntity}.
 */
@Service
@Transactional
public class BoardUpdateSuccessorServiceImpl implements BoardUpdateSuccessorService {

    private final Logger log = LoggerFactory.getLogger(BoardUpdateSuccessorServiceImpl.class);

    private final BoardUpdateSuccessorRepository boardUpdateSuccessorRepository;

    public BoardUpdateSuccessorServiceImpl(BoardUpdateSuccessorRepository boardUpdateSuccessorRepository) {
        this.boardUpdateSuccessorRepository = boardUpdateSuccessorRepository;
    }

    @Override
    public BoardUpdateSuccessorEntity save(BoardUpdateSuccessorEntity boardUpdateSuccessorEntity) {
        log.debug("Request to save BoardUpdateSuccessor : {}", boardUpdateSuccessorEntity);
        return boardUpdateSuccessorRepository.save(boardUpdateSuccessorEntity);
    }

    @Override
    public Optional<BoardUpdateSuccessorEntity> partialUpdate(BoardUpdateSuccessorEntity boardUpdateSuccessorEntity) {
        log.debug("Request to partially update BoardUpdateSuccessor : {}", boardUpdateSuccessorEntity);

        return boardUpdateSuccessorRepository
            .findById(boardUpdateSuccessorEntity.getId())
            .map(
                existingBoardUpdateSuccessor -> {
                    return existingBoardUpdateSuccessor;
                }
            )
            .map(boardUpdateSuccessorRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardUpdateSuccessorEntity> findAll() {
        log.debug("Request to get all BoardUpdateSuccessors");
        return boardUpdateSuccessorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BoardUpdateSuccessorEntity> findOne(Long id) {
        log.debug("Request to get BoardUpdateSuccessor : {}", id);
        return boardUpdateSuccessorRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BoardUpdateSuccessor : {}", id);
        boardUpdateSuccessorRepository.deleteById(id);
    }
}
