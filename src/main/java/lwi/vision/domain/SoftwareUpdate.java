package lwi.vision.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SoftwareUpdate.
 */
@Entity
@Table(name = "software_update")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SoftwareUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    @JsonIgnoreProperties(value = { "software", "firmware" }, allowSetters = true)
    private Board board;

    @ManyToOne
    @JsonIgnoreProperties(value = { "board" }, allowSetters = true)
    private Software from;

    @ManyToOne
    @JsonIgnoreProperties(value = { "board" }, allowSetters = true)
    private Software to;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SoftwareUpdate id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getActive() {
        return this.active;
    }

    public SoftwareUpdate active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Board getBoard() {
        return this.board;
    }

    public SoftwareUpdate board(Board board) {
        this.setBoard(board);
        return this;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Software getFrom() {
        return this.from;
    }

    public SoftwareUpdate from(Software software) {
        this.setFrom(software);
        return this;
    }

    public void setFrom(Software software) {
        this.from = software;
    }

    public Software getTo() {
        return this.to;
    }

    public SoftwareUpdate to(Software software) {
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
        if (!(o instanceof SoftwareUpdate)) {
            return false;
        }
        return id != null && id.equals(((SoftwareUpdate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SoftwareUpdate{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
