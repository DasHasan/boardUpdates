package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.BoardUpdateEntity;
import lwi.vision.domain.UpdateKeysEntity;
import lwi.vision.domain.UpdatePreconditionEntity;
import lwi.vision.repository.BoardUpdateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BoardUpdateEntity}.
 */
@Service
@Transactional
public class BoardUpdateService {

    private final Logger log = LoggerFactory.getLogger(BoardUpdateService.class);

    private final BoardUpdateRepository boardUpdateRepository;

    public BoardUpdateService(BoardUpdateRepository boardUpdateRepository) {
        this.boardUpdateRepository = boardUpdateRepository;
    }

    /**
     * Save a boardUpdate.
     *
     * @param boardUpdateEntity the entity to save.
     * @return the persisted entity.
     */
    public BoardUpdateEntity save(BoardUpdateEntity boardUpdateEntity) {
        log.debug("Request to save BoardUpdate : {}", boardUpdateEntity);
        if (boardUpdateEntity.getUpdatePrecondition() == null) {
            boardUpdateEntity.setUpdatePrecondition(new UpdatePreconditionEntity().boardUpdate(boardUpdateEntity));
        }
        return boardUpdateRepository.save(boardUpdateEntity);
    }

    /**
     * Partially update a boardUpdate.
     *
     * @param boardUpdateEntity the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BoardUpdateEntity> partialUpdate(BoardUpdateEntity boardUpdateEntity) {
        log.debug("Request to partially update BoardUpdate : {}", boardUpdateEntity);

        return boardUpdateRepository
            .findById(boardUpdateEntity.getId())
            .map(
                existingBoardUpdate -> {
                    if (boardUpdateEntity.getVersion() != null) {
                        existingBoardUpdate.setVersion(boardUpdateEntity.getVersion());
                    }
                    if (boardUpdateEntity.getPath() != null) {
                        existingBoardUpdate.setPath(boardUpdateEntity.getPath());
                    }
                    if (boardUpdateEntity.getType() != null) {
                        existingBoardUpdate.setType(boardUpdateEntity.getType());
                    }
                    if (boardUpdateEntity.getReleaseDate() != null) {
                        existingBoardUpdate.setReleaseDate(boardUpdateEntity.getReleaseDate());
                    }
                    if (boardUpdateEntity.getStatus() != null) {
                        existingBoardUpdate.setStatus(boardUpdateEntity.getStatus());
                    }

                    return existingBoardUpdate;
                }
            )
            .map(boardUpdateRepository::save);
    }

    /**
     * Get all the boardUpdates.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BoardUpdateEntity> findAll() {
        log.debug("Request to get all BoardUpdates");
        return boardUpdateRepository.findAll();
    }

    /**
     * Get one boardUpdate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BoardUpdateEntity> findOne(Long id) {
        log.debug("Request to get BoardUpdate : {}", id);
        return boardUpdateRepository.findById(id);
    }

    /**
     * Delete the boardUpdate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BoardUpdate : {}", id);
        boardUpdateRepository.deleteById(id);
    }
}
