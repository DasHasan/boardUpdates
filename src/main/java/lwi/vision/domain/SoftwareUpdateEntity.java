package lwi.vision.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SoftwareUpdateEntity.
 */
@Entity
@Table(name = "software_update")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SoftwareUpdateEntity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    @JsonIgnoreProperties(value = { "software", "firmware" }, allowSetters = true)
    private BoardEntity board;

    @ManyToOne
    @JsonIgnoreProperties(value = { "board" }, allowSetters = true)
    private SoftwareEntity from;

    @ManyToOne
    @JsonIgnoreProperties(value = { "board" }, allowSetters = true)
    private SoftwareEntity to;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SoftwareUpdateEntity id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getActive() {
        return this.active;
    }

    public SoftwareUpdateEntity active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public BoardEntity getBoard() {
        return this.board;
    }

    public SoftwareUpdateEntity board(BoardEntity board) {
        this.setBoard(board);
        return this;
    }

    public void setBoard(BoardEntity board) {
        this.board = board;
    }

    public SoftwareEntity getFrom() {
        return this.from;
    }

    public SoftwareUpdateEntity from(SoftwareEntity software) {
        this.setFrom(software);
        return this;
    }

    public void setFrom(SoftwareEntity software) {
        this.from = software;
    }

    public SoftwareEntity getTo() {
        return this.to;
    }

    public SoftwareUpdateEntity to(SoftwareEntity software) {
        this.setTo(software);
        return this;
    }

    public void setTo(SoftwareEntity software) {
        this.to = software;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SoftwareUpdateEntity)) {
            return false;
        }
        return id != null && id.equals(((SoftwareUpdateEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SoftwareUpdateEntity{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
