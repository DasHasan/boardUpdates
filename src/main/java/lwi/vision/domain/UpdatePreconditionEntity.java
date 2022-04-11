package lwi.vision.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UpdatePreconditionEntity.
 */
@Entity
@Table(name = "update_precondition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UpdatePreconditionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = { "updatePrecondition", "downloadUrl" }, allowSetters = true)
    private BoardUpdateEntity boardUpdate;

    @OneToMany(mappedBy = "updatePrecondition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "updatePrecondition" }, allowSetters = true)
    private Set<UpdateKeysEntity> updateKeys = new HashSet<>();

    @OneToMany(mappedBy = "updatePrecondition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "updatePrecondition" }, allowSetters = true)
    private Set<BoardEntity> boards = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UpdatePreconditionEntity id(Long id) {
        this.id = id;
        return this;
    }

    public BoardUpdateEntity getBoardUpdate() {
        return this.boardUpdate;
    }

    public UpdatePreconditionEntity boardUpdate(BoardUpdateEntity boardUpdate) {
        this.setBoardUpdate(boardUpdate);
        return this;
    }

    public void setBoardUpdate(BoardUpdateEntity boardUpdate) {
        this.boardUpdate = boardUpdate;
    }

    public Set<UpdateKeysEntity> getUpdateKeys() {
        return this.updateKeys;
    }

    public UpdatePreconditionEntity updateKeys(Set<UpdateKeysEntity> updateKeys) {
        this.setUpdateKeys(updateKeys);
        return this;
    }

    public UpdatePreconditionEntity addUpdateKeys(UpdateKeysEntity updateKeys) {
        this.updateKeys.add(updateKeys);
        updateKeys.setUpdatePrecondition(this);
        return this;
    }

    public UpdatePreconditionEntity removeUpdateKeys(UpdateKeysEntity updateKeys) {
        this.updateKeys.remove(updateKeys);
        updateKeys.setUpdatePrecondition(null);
        return this;
    }

    public void setUpdateKeys(Set<UpdateKeysEntity> updateKeys) {
        if (this.updateKeys != null) {
            this.updateKeys.forEach(i -> i.setUpdatePrecondition(null));
        }
        if (updateKeys != null) {
            updateKeys.forEach(i -> i.setUpdatePrecondition(this));
        }
        this.updateKeys = updateKeys;
    }

    public Set<BoardEntity> getBoards() {
        return this.boards;
    }

    public UpdatePreconditionEntity boards(Set<BoardEntity> boards) {
        this.setBoards(boards);
        return this;
    }

    public UpdatePreconditionEntity addBoards(BoardEntity board) {
        this.boards.add(board);
        board.setUpdatePrecondition(this);
        return this;
    }

    public UpdatePreconditionEntity removeBoards(BoardEntity board) {
        this.boards.remove(board);
        board.setUpdatePrecondition(null);
        return this;
    }

    public void setBoards(Set<BoardEntity> boards) {
        if (this.boards != null) {
            this.boards.forEach(i -> i.setUpdatePrecondition(null));
        }
        if (boards != null) {
            boards.forEach(i -> i.setUpdatePrecondition(this));
        }
        this.boards = boards;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UpdatePreconditionEntity)) {
            return false;
        }
        return id != null && id.equals(((UpdatePreconditionEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UpdatePreconditionEntity{" +
            "id=" + getId() +
            "}";
    }
}
