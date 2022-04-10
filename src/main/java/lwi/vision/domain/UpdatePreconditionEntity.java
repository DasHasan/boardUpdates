package lwi.vision.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
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
    @JsonIgnoreProperties(value = { "updateKeys", "board" }, allowSetters = true)
    private BoardUpdateEntity boardUpdate;

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
