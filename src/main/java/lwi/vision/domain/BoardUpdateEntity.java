package lwi.vision.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lwi.vision.domain.enumeration.UpdateType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A BoardUpdateEntity.
 */
@Entity
@Table(name = "board_update")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BoardUpdateEntity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "version")
    private String version = "";

    @Column(name = "path")
    private String path = "";

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UpdateType type;

    @Column(name = "release_date")
    private ZonedDateTime releaseDate = ZonedDateTime.now();

    @Column(name = "status")
    private String status = "";

    @OneToMany(mappedBy = "boardUpdate", cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.NONE) // fix entity changes
    @JsonIgnoreProperties(value = { "boardUpdate" }, allowSetters = true)
    private Set<UpdateKeysEntity> updateKeys = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "boardUpdates" }, allowSetters = true)
    private BoardEntity board;

    @JsonIgnoreProperties(value = { "boardUpdate" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private UpdatePreconditionEntity updatePrecondition;

    @OneToOne(mappedBy = "boardUpdate", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties(value = { "boardUpdate" }, allowSetters = true)
    private DownloadUrlEntity downloadUrlEntity;

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

    public Set<UpdateKeysEntity> getUpdateKeys() {
        return this.updateKeys;
    }

    public BoardUpdateEntity updateKeys(Set<UpdateKeysEntity> updateKeys) {
        this.setUpdateKeys(updateKeys);
        return this;
    }

    public BoardUpdateEntity addUpdateKeys(UpdateKeysEntity updateKeys) {
        this.updateKeys.add(updateKeys);
        updateKeys.setBoardUpdate(this);
        return this;
    }

    public BoardUpdateEntity removeUpdateKeys(UpdateKeysEntity updateKeys) {
        this.updateKeys.remove(updateKeys);
        updateKeys.setBoardUpdate(null);
        return this;
    }

    public void setUpdateKeys(Set<UpdateKeysEntity> updateKeys) {
        if (this.updateKeys != null) {
            this.updateKeys.forEach(i -> i.setBoardUpdate(null));
        }
        if (updateKeys != null) {
            updateKeys.forEach(i -> i.setBoardUpdate(this));
        }
        this.updateKeys = updateKeys;
    }

    public BoardEntity getBoard() {
        return this.board;
    }

    public BoardUpdateEntity board(BoardEntity board) {
        this.setBoard(board);
        return this;
    }

    public void setBoard(BoardEntity board) {
        this.board = board;
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

    public DownloadUrlEntity getDownloadUrlEntity() {
        return downloadUrlEntity;
    }

    public void setDownloadUrlEntity(DownloadUrlEntity downloadUrlEntity) {
        this.downloadUrlEntity = downloadUrlEntity;
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

    @Override
    public String toString() {
        return "BoardUpdateEntity{" +
            "id=" + id +
            ", version='" + version + '\'' +
            ", path='" + path + '\'' +
            ", type=" + type +
            ", releaseDate=" + releaseDate +
            ", status='" + status + '\'' +
            ", updateKeys=" + updateKeys +
            ", board=" + board +
            ", updatePrecondition=" + updatePrecondition +
            ", downloadUrlEntity=" + downloadUrlEntity +
            '}';
    }
}
