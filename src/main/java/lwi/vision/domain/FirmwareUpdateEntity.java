package lwi.vision.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FirmwareUpdateEntity.
 */
@Entity
@Table(name = "firmware_update")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FirmwareUpdateEntity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    @JsonIgnoreProperties(value = { "firmware", "software" }, allowSetters = true)
    private BoardEntity board;

    @ManyToOne
    @JsonIgnoreProperties(value = { "board" }, allowSetters = true)
    private FirmwareEntity from;

    @ManyToOne
    @JsonIgnoreProperties(value = { "board" }, allowSetters = true)
    private FirmwareEntity to;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FirmwareUpdateEntity id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getActive() {
        return this.active;
    }

    public FirmwareUpdateEntity active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public BoardEntity getBoard() {
        return this.board;
    }

    public FirmwareUpdateEntity board(BoardEntity board) {
        this.setBoard(board);
        return this;
    }

    public void setBoard(BoardEntity board) {
        this.board = board;
    }

    public FirmwareEntity getFrom() {
        return this.from;
    }

    public FirmwareUpdateEntity from(FirmwareEntity firmware) {
        this.setFrom(firmware);
        return this;
    }

    public void setFrom(FirmwareEntity firmware) {
        this.from = firmware;
    }

    public FirmwareEntity getTo() {
        return this.to;
    }

    public FirmwareUpdateEntity to(FirmwareEntity firmware) {
        this.setTo(firmware);
        return this;
    }

    public void setTo(FirmwareEntity firmware) {
        this.to = firmware;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FirmwareUpdateEntity)) {
            return false;
        }
        return id != null && id.equals(((FirmwareUpdateEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FirmwareUpdateEntity{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
