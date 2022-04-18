package lwi.vision.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DownloadUrlEntity.
 */
@Entity
@Table(name = "download_url")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DownloadUrlEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "url")
    private String url = UUID.randomUUID().toString();

    @JsonIgnoreProperties(value = { "updatePrecondition", "downloadUrl" }, allowSetters = true)
    @OneToOne(mappedBy = "downloadUrl", cascade = CascadeType.PERSIST)
    private BoardUpdateEntity boardUpdate;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DownloadUrlEntity id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public DownloadUrlEntity expirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getUrl() {
        return this.url;
    }

    public DownloadUrlEntity url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BoardUpdateEntity getBoardUpdate() {
        return this.boardUpdate;
    }

    public DownloadUrlEntity boardUpdate(BoardUpdateEntity boardUpdate) {
        this.setBoardUpdate(boardUpdate);
        return this;
    }

    public void setBoardUpdate(BoardUpdateEntity boardUpdate) {
        if (this.boardUpdate != null) {
            this.boardUpdate.setDownloadUrl(null);
        }
        if (boardUpdate != null) {
            boardUpdate.setDownloadUrl(this);
        }
        this.boardUpdate = boardUpdate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DownloadUrlEntity)) {
            return false;
        }
        return id != null && id.equals(((DownloadUrlEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DownloadUrlEntity{" +
            "id=" + getId() +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
