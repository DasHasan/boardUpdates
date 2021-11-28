package lwi.vision.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link lwi.vision.domain.BoardUpdateSuccessorEntity} entity. This class is used
 * in {@link lwi.vision.web.rest.BoardUpdateSuccessorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /board-update-successors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BoardUpdateSuccessorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter fromId;

    private LongFilter toId;

    public BoardUpdateSuccessorCriteria() {}

    public BoardUpdateSuccessorCriteria(BoardUpdateSuccessorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fromId = other.fromId == null ? null : other.fromId.copy();
        this.toId = other.toId == null ? null : other.toId.copy();
    }

    @Override
    public BoardUpdateSuccessorCriteria copy() {
        return new BoardUpdateSuccessorCriteria(this);
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

    public LongFilter getFromId() {
        return fromId;
    }

    public LongFilter fromId() {
        if (fromId == null) {
            fromId = new LongFilter();
        }
        return fromId;
    }

    public void setFromId(LongFilter fromId) {
        this.fromId = fromId;
    }

    public LongFilter getToId() {
        return toId;
    }

    public LongFilter toId() {
        if (toId == null) {
            toId = new LongFilter();
        }
        return toId;
    }

    public void setToId(LongFilter toId) {
        this.toId = toId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BoardUpdateSuccessorCriteria that = (BoardUpdateSuccessorCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(fromId, that.fromId) && Objects.equals(toId, that.toId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromId, toId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BoardUpdateSuccessorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fromId != null ? "fromId=" + fromId + ", " : "") +
            (toId != null ? "toId=" + toId + ", " : "") +
            "}";
    }
}
