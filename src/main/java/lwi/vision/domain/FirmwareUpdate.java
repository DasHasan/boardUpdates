package lwi.vision.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FirmwareUpdate.
 */
@Entity
@Table(name = "firmware_update")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FirmwareUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    private Board board;

    @ManyToOne
    private Software from;

    @ManyToOne
    private Software to;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FirmwareUpdate id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getActive() {
        return this.active;
    }

    public FirmwareUpdate active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Board getBoard() {
        return this.board;
    }

    public FirmwareUpdate board(Board board) {
        this.setBoard(board);
        return this;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Software getFrom() {
        return this.from;
    }

    public FirmwareUpdate from(Software software) {
        this.setFrom(software);
        return this;
    }

    public void setFrom(Software software) {
        this.from = software;
    }

    public Software getTo() {
        return this.to;
    }

    public FirmwareUpdate to(Software software) {
        this.setTo(software);
        return this;
    }

    public void setTo(Software software) {
        this.to = software;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FirmwareUpdate)) {
            return false;
        }
        return id != null && id.equals(((FirmwareUpdate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FirmwareUpdate{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
