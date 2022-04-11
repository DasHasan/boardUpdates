package lwi.vision.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UpdateKeysEntity.
 */
@Entity
@Table(name = "update_keys")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UpdateKeysEntity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_key")
    private String key;

    @ManyToOne
    @JsonIgnoreProperties(value = { "updateKeys", "board", "updatePrecondition" }, allowSetters = true)
    private BoardUpdateEntity boardUpdate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "boardUpdate", "updateKeys" }, allowSetters = true)
    private UpdatePreconditionEntity updatePrecondition;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UpdateKeysEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return this.key;
    }

    public UpdateKeysEntity key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BoardUpdateEntity getBoardUpdate() {
        return this.boardUpdate;
    }

    public UpdateKeysEntity boardUpdate(BoardUpdateEntity boardUpdate) {
        this.setBoardUpdate(boardUpdate);
        return this;
    }

    public void setBoardUpdate(BoardUpdateEntity boardUpdate) {
        this.boardUpdate = boardUpdate;
    }

    public UpdatePreconditionEntity getUpdatePrecondition() {
        return this.updatePrecondition;
    }

    public UpdateKeysEntity updatePrecondition(UpdatePreconditionEntity updatePrecondition) {
        this.setUpdatePrecondition(updatePrecondition);
        return this;
    }

    public void setUpdatePrecondition(UpdatePreconditionEntity updatePrecondition) {
        this.updatePrecondition = updatePrecondition;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UpdateKeysEntity)) {
            return false;
        }
        return id != null && id.equals(((UpdateKeysEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UpdateKeysEntity{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            "}";
    }
}
