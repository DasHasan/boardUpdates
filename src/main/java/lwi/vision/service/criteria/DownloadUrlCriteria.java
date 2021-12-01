package lwi.vision.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link lwi.vision.domain.DownloadUrlEntity} entity. This class is used
 * in {@link lwi.vision.web.rest.DownloadUrlResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /download-urls?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DownloadUrlCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter expirationDate;

    private StringFilter url;

    private LongFilter boardUpdateId;

    public DownloadUrlCriteria() {}

    public DownloadUrlCriteria(DownloadUrlCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.expirationDate = other.expirationDate == null ? null : other.expirationDate.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.boardUpdateId = other.boardUpdateId == null ? null : other.boardUpdateId.copy();
    }

    @Override
    public DownloadUrlCriteria copy() {
        return new DownloadUrlCriteria(this);
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

    public LocalDateFilter getExpirationDate() {
        return expirationDate;
    }

    public LocalDateFilter expirationDate() {
        if (expirationDate == null) {
            expirationDate = new LocalDateFilter();
        }
        return expirationDate;
    }

    public void setExpirationDate(LocalDateFilter expirationDate) {
        this.expirationDate = expirationDate;
    }

    public StringFilter getUrl() {
        return url;
    }

    public StringFilter url() {
        if (url == null) {
            url = new StringFilter();
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
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
        final DownloadUrlCriteria that = (DownloadUrlCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(url, that.url) &&
            Objects.equals(boardUpdateId, that.boardUpdateId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, expirationDate, url, boardUpdateId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DownloadUrlCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (expirationDate != null ? "expirationDate=" + expirationDate + ", " : "") +
            (url != null ? "url=" + url + ", " : "") +
            (boardUpdateId != null ? "boardUpdateId=" + boardUpdateId + ", " : "") +
            "}";
    }
}
