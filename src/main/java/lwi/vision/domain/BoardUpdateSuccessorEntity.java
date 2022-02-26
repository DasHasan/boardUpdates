package lwi.vision.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BoardUpdateSuccessorEntity.
 */
@Entity
@Table(name = "board_update_successor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BoardUpdateSuccessorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties(value = { "updateKeys", "board" }, allowSetters = true)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private BoardUpdateEntity from;

    @JsonIgnoreProperties(value = { "updateKeys", "board" }, allowSetters = true)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private BoardUpdateEntity to;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BoardUpdateSuccessorEntity id(Long id) {
        this.id = id;
        return this;
    }

    public BoardUpdateEntity getFrom() {
        return this.from;
    }

    public BoardUpdateSuccessorEntity from(BoardUpdateEntity boardUpdate) {
        this.setFrom(boardUpdate);
        return this;
    }

    public void setFrom(BoardUpdateEntity boardUpdate) {
        this.from = boardUpdate;
    }

    public BoardUpdateEntity getTo() {
        return this.to;
    }

    public BoardUpdateSuccessorEntity to(BoardUpdateEntity boardUpdate) {
        this.setTo(boardUpdate);
        return this;
    }

    public void setTo(BoardUpdateEntity boardUpdate) {
        this.to = boardUpdate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BoardUpdateSuccessorEntity)) {
            return false;
        }
        return id != null && id.equals(((BoardUpdateSuccessorEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BoardUpdateSuccessorEntity{" +
            "id=" + getId() +
            "}";
    }
}
