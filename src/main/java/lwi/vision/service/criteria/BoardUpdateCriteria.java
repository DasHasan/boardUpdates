package lwi.vision.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import lwi.vision.domain.enumeration.UpdateType;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link lwi.vision.domain.BoardUpdateEntity} entity. This class is used
 * in {@link lwi.vision.web.rest.BoardUpdateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /board-updates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BoardUpdateCriteria implements Serializable, Criteria {

    /**
     * Class for filtering UpdateType
     */
    public static class UpdateTypeFilter extends Filter<UpdateType> {

        public UpdateTypeFilter() {}

        public UpdateTypeFilter(UpdateTypeFilter filter) {
            super(filter);
        }

        @Override
        public UpdateTypeFilter copy() {
            return new UpdateTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter version;

    private StringFilter path;

    private UpdateTypeFilter type;

    private ZonedDateTimeFilter releaseDate;

    private LongFilter updateKeysId;

    private LongFilter boardId;

    public BoardUpdateCriteria() {}

    public BoardUpdateCriteria(BoardUpdateCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.version = other.version == null ? null : other.version.copy();
        this.path = other.path == null ? null : other.path.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.releaseDate = other.releaseDate == null ? null : other.releaseDate.copy();
        this.updateKeysId = other.updateKeysId == null ? null : other.updateKeysId.copy();
        this.boardId = other.boardId == null ? null : other.boardId.copy();
    }

    @Override
    public BoardUpdateCriteria copy() {
        return new BoardUpdateCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getVersion() {
        return version;
    }

    public StringFilter version() {
        if (version == null) {
            version = new StringFilter();
        }
        return version;
    }

    public void setVersion(StringFilter version) {
        this.version = version;
    }

    public StringFilter getPath() {
        return path;
    }

    public StringFilter path() {
        if (path == null) {
            path = new StringFilter();
        }
        return path;
    }

    public void setPath(StringFilter path) {
        this.path = path;
    }

    public UpdateTypeFilter getType() {
        return type;
    }

    public UpdateTypeFilter type() {
        if (type == null) {
            type = new UpdateTypeFilter();
        }
        return type;
    }

    public void setType(UpdateTypeFilter type) {
        this.type = type;
    }

    public ZonedDateTimeFilter getReleaseDate() {
        return releaseDate;
    }

    public ZonedDateTimeFilter releaseDate() {
        if (releaseDate == null) {
            releaseDate = new ZonedDateTimeFilter();
        }
        return releaseDate;
    }

    public void setReleaseDate(ZonedDateTimeFilter releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LongFilter getUpdateKeysId() {
        return updateKeysId;
    }

    public LongFilter updateKeysId() {
        if (updateKeysId == null) {
            updateKeysId = new LongFilter();
        }
        return updateKeysId;
    }

    public void setUpdateKeysId(LongFilter updateKeysId) {
        this.updateKeysId = updateKeysId;
    }

    public LongFilter getBoardId() {
        return boardId;
    }

    public LongFilter boardId() {
        if (boardId == null) {
            boardId = new LongFilter();
        }
        return boardId;
    }

    public void setBoardId(LongFilter boardId) {
        this.boardId = boardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BoardUpdateCriteria that = (BoardUpdateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(version, that.version) &&
            Objects.equals(path, that.path) &&
            Objects.equals(type, that.type) &&
            Objects.equals(releaseDate, that.releaseDate) &&
            Objects.equals(updateKeysId, that.updateKeysId) &&
            Objects.equals(boardId, that.boardId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, path, type, releaseDate, updateKeysId, boardId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BoardUpdateCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (version != null ? "version=" + version + ", " : "") +
            (path != null ? "path=" + path + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (releaseDate != null ? "releaseDate=" + releaseDate + ", " : "") +
            (updateKeysId != null ? "updateKeysId=" + updateKeysId + ", " : "") +
            (boardId != null ? "boardId=" + boardId + ", " : "") +
            "}";
    }
}
