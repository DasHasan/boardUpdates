package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.Board;
import lwi.vision.repository.BoardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Board}.
 */
@Service
@Transactional
public class BoardService {

    private final Logger log = LoggerFactory.getLogger(BoardService.class);

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * Save a board.
     *
     * @param board the entity to save.
     * @return the persisted entity.
     */
    public Board save(Board board) {
        log.debug("Request to save Board : {}", board);
        return boardRepository.save(board);
    }

    /**
     * Partially update a board.
     *
     * @param board the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Board> partialUpdate(Board board) {
        log.debug("Request to partially update Board : {}", board);

        return boardRepository
            .findById(board.getId())
            .map(
                existingBoard -> {
                    if (board.getSerial() != null) {
                        existingBoard.setSerial(board.getSerial());
                    }

                    return existingBoard;
                }
            )
            .map(boardRepository::save);
    }

    /**
     * Get all the boards.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Board> findAll() {
        log.debug("Request to get all Boards");
        return boardRepository.findAll();
    }

    /**
     * Get one board by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Board> findOne(Long id) {
        log.debug("Request to get Board : {}", id);
        return boardRepository.findById(id);
    }

    /**
     * Delete the board by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Board : {}", id);
        boardRepository.deleteById(id);
    }
}
