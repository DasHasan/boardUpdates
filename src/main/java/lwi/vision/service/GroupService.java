package lwi.vision.service;

import java.util.List;
import java.util.Optional;
import lwi.vision.domain.Group;
import lwi.vision.repository.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Group}.
 */
@Service
@Transactional
public class GroupService {

    private final Logger log = LoggerFactory.getLogger(GroupService.class);

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /**
     * Save a group.
     *
     * @param group the entity to save.
     * @return the persisted entity.
     */
    public Group save(Group group) {
        log.debug("Request to save Group : {}", group);
        return groupRepository.save(group);
    }

    /**
     * Partially update a group.
     *
     * @param group the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Group> partialUpdate(Group group) {
        log.debug("Request to partially update Group : {}", group);

        return groupRepository
            .findById(group.getId())
            .map(
                existingGroup -> {
                    if (group.getName() != null) {
                        existingGroup.setName(group.getName());
                    }

                    return existingGroup;
                }
            )
            .map(groupRepository::save);
    }

    /**
     * Get all the groups.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Group> findAll() {
        log.debug("Request to get all Groups");
        return groupRepository.findAll();
    }

    /**
     * Get one group by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Group> findOne(Long id) {
        log.debug("Request to get Group : {}", id);
        return groupRepository.findById(id);
    }

    /**
     * Delete the group by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Group : {}", id);
        groupRepository.deleteById(id);
    }
}
