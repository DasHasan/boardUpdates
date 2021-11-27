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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link lwi.vision.domain.DownloadEntity} entity. This class is used
 * in {@link lwi.vision.web.rest.DownloadResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /downloads?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DownloadCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter date;

    private LongFilter boardUpdateId;

    public DownloadCriteria() {}

    public DownloadCriteria(DownloadCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.boardUpdateId = other.boardUpdateId == null ? null : other.boardUpdateId.copy();
    }

    @Override
    public DownloadCriteria copy() {
        return new DownloadCriteria(this);
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

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public ZonedDateTimeFilter date() {
        if (date == null) {
            date = new ZonedDateTimeFilter();
        }
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public LongFilter getBoardUpdateId() {
        return boardUpdateId;
    }

    public LongFilter boardUpdateId() {
        if (boardUpdateId == null) {
            boardUpdateId = new LongFilter();
        }
        return boardUpdateId;
    }

    public void setBoardUpdateId(LongFilter boardUpdateId) {
        this.boardUpdateId = boardUpdateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DownloadCriteria that = (DownloadCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(date, that.date) && Objects.equals(boardUpdateId, that.boardUpdateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, boardUpdateId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DownloadCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (boardUpdateId != null ? "boardUpdateId=" + boardUpdateId + ", " : "") +
            "}";
    }
}
