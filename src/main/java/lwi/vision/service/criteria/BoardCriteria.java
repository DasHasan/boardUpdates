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
 * Criteria class for the {@link lwi.vision.domain.BoardEntity} entity. This class is used
 * in {@link lwi.vision.web.rest.BoardResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /boards?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BoardCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter serial;

    private LongFilter firmwareId;

    private LongFilter softwareId;

    public BoardCriteria() {}

    public BoardCriteria(BoardCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.serial = other.serial == null ? null : other.serial.copy();
        this.firmwareId = other.firmwareId == null ? null : other.firmwareId.copy();
        this.softwareId = other.softwareId == null ? null : other.softwareId.copy();
    }

    @Override
    public BoardCriteria copy() {
        return new BoardCriteria(this);
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

    public StringFilter getSerial() {
        return serial;
    }

    public StringFilter serial() {
        if (serial == null) {
            serial = new StringFilter();
        }
        return serial;
    }

    public void setSerial(StringFilter serial) {
        this.serial = serial;
    }

    public LongFilter getFirmwareId() {
        return firmwareId;
    }

    public LongFilter firmwareId() {
        if (firmwareId == null) {
            firmwareId = new LongFilter();
        }
        return firmwareId;
    }

    public void setFirmwareId(LongFilter firmwareId) {
        this.firmwareId = firmwareId;
    }

    public LongFilter getSoftwareId() {
        return softwareId;
    }

    public LongFilter softwareId() {
        if (softwareId == null) {
            softwareId = new LongFilter();
        }
        return softwareId;
    }

    public void setSoftwareId(LongFilter softwareId) {
        this.softwareId = softwareId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BoardCriteria that = (BoardCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(serial, that.serial) &&
            Objects.equals(firmwareId, that.firmwareId) &&
            Objects.equals(softwareId, that.softwareId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serial, firmwareId, softwareId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BoardCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (serial != null ? "serial=" + serial + ", " : "") +
            (firmwareId != null ? "firmwareId=" + firmwareId + ", " : "") +
            (softwareId != null ? "softwareId=" + softwareId + ", " : "") +
            "}";
    }
}