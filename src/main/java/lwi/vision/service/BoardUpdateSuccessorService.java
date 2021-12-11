package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.BoardUpdateSuccessorEntity;
import lwi.vision.repository.BoardUpdateSuccessorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BoardUpdateSuccessorEntity}.
 */
@Service
@Transactional
public class BoardUpdateSuccessorService {

    private final Logger log = LoggerFactory.getLogger(BoardUpdateSuccessorService.class);

    private final BoardUpdateSuccessorRepository boardUpdateSuccessorRepository;

    public BoardUpdateSuccessorService(BoardUpdateSuccessorRepository boardUpdateSuccessorRepository) {
        this.boardUpdateSuccessorRepository = boardUpdateSuccessorRepository;
    }

    /**
     * Save a boardUpdateSuccessor.
     *
     * @param boardUpdateSuccessorEntity the entity to save.
     * @return the persisted entity.
     */
    public BoardUpdateSuccessorEntity save(BoardUpdateSuccessorEntity boardUpdateSuccessorEntity) {
        log.debug("Request to save BoardUpdateSuccessor : {}", boardUpdateSuccessorEntity);
        return boardUpdateSuccessorRepository.save(boardUpdateSuccessorEntity);
    }

    /**
     * Partially update a boardUpdateSuccessor.
     *
     * @param boardUpdateSuccessorEntity the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get all the boardUpdateSuccessors.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BoardUpdateSuccessorEntity> findAll() {
        log.debug("Request to get all BoardUpdateSuccessors");
        return boardUpdateSuccessorRepository.findAll();
    }

    /**
     * Get one boardUpdateSuccessor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BoardUpdateSuccessorEntity> findOne(Long id) {
        log.debug("Request to get BoardUpdateSuccessor : {}", id);
        return boardUpdateSuccessorRepository.findById(id);
    }

    /**
     * Delete the boardUpdateSuccessor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BoardUpdateSuccessor : {}", id);
        boardUpdateSuccessorRepository.deleteById(id);
    }
}
