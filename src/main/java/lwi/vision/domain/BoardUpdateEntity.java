package lwi.vision.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import lwi.vision.domain.enumeration.UpdateType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BoardUpdateEntity.
 */
@Entity
@Table(name = "board_update")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BoardUpdateEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "version")
    private String version;

    @Column(name = "path")
    private String path;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UpdateType type;

    @Column(name = "release_date")
    private ZonedDateTime releaseDate;

    @Column(name = "status")
    private String status;

    @JsonIgnoreProperties(value = { "updateKeys", "boards" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private UpdatePreconditionEntity updatePrecondition;

    @JsonIgnoreProperties(value = { "boardUpdate" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private DownloadUrlEntity downloadUrl;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BoardUpdateEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getVersion() {
        return this.version;
    }

    public BoardUpdateEntity version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return this.path;
    }

    public BoardUpdateEntity path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public UpdateType getType() {
        return this.type;
    }

    public BoardUpdateEntity type(UpdateType type) {
        this.type = type;
        return this;
    }

    public void setType(UpdateType type) {
        this.type = type;
    }

    public ZonedDateTime getReleaseDate() {
        return this.releaseDate;
    }

    public BoardUpdateEntity releaseDate(ZonedDateTime releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public void setReleaseDate(ZonedDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getStatus() {
        return this.status;
    }

    public BoardUpdateEntity status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UpdatePreconditionEntity getUpdatePrecondition() {
        return this.updatePrecondition;
    }

    public BoardUpdateEntity updatePrecondition(UpdatePreconditionEntity updatePrecondition) {
        this.setUpdatePrecondition(updatePrecondition);
        return this;
    }

    public void setUpdatePrecondition(UpdatePreconditionEntity updatePrecondition) {
        this.updatePrecondition = updatePrecondition;
    }

    public DownloadUrlEntity getDownloadUrl() {
        return this.downloadUrl;
    }

    public BoardUpdateEntity downloadUrl(DownloadUrlEntity downloadUrl) {
        this.setDownloadUrl(downloadUrl);
        return this;
    }

    public void setDownloadUrl(DownloadUrlEntity downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BoardUpdateEntity)) {
            return false;
        }
        return id != null && id.equals(((BoardUpdateEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BoardUpdateEntity{" +
            "id=" + getId() +
            ", version='" + getVersion() + "'" +
            ", path='" + getPath() + "'" +
            ", type='" + getType() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
