package lwi.vision.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DownloadEntity.
 */
@Entity
@Table(name = "download")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DownloadEntity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private ZonedDateTime date;

    @JsonIgnoreProperties(value = { "updateKeys", "board" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private BoardUpdateEntity boardUpdate;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DownloadEntity id(Long id) {
        this.id = id;
        return this;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public DownloadEntity date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public BoardUpdateEntity getBoardUpdate() {
        return this.boardUpdate;
    }

    public DownloadEntity boardUpdate(BoardUpdateEntity boardUpdate) {
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
        if (!(o instanceof DownloadEntity)) {
            return false;
        }
        return id != null && id.equals(((DownloadEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DownloadEntity{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
