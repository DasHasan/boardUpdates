package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.BoardUpdateSuccessorEntity;

/**
 * Service Interface for managing {@link BoardUpdateSuccessorEntity}.
 */
public interface BoardUpdateSuccessorService {
    /**
     * Save a boardUpdateSuccessor.
     *
     * @param boardUpdateSuccessorEntity the entity to save.
     * @return the persisted entity.
     */
    BoardUpdateSuccessorEntity save(BoardUpdateSuccessorEntity boardUpdateSuccessorEntity);

    /**
     * Partially updates a boardUpdateSuccessor.
     *
     * @param boardUpdateSuccessorEntity the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BoardUpdateSuccessorEntity> partialUpdate(BoardUpdateSuccessorEntity boardUpdateSuccessorEntity);

    /**
     * Get all the boardUpdateSuccessors.
     *
     * @return the list of entities.
     */
    List<BoardUpdateSuccessorEntity> findAll();

    /**
     * Get the "id" boardUpdateSuccessor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BoardUpdateSuccessorEntity> findOne(Long id);

    /**
     * Delete the "id" boardUpdateSuccessor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
