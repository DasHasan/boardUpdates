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
 * Criteria class for the {@link lwi.vision.domain.UpdateKeysEntity} entity. This class is used
 * in {@link lwi.vision.web.rest.UpdateKeysResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /update-keys?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UpdateKeysCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter key;

    private LongFilter updatePreconditionId;

    public UpdateKeysCriteria() {}

    public UpdateKeysCriteria(UpdateKeysCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.key = other.key == null ? null : other.key.copy();
        this.updatePreconditionId = other.updatePreconditionId == null ? null : other.updatePreconditionId.copy();
    }

    @Override
    public UpdateKeysCriteria copy() {
        return new UpdateKeysCriteria(this);
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

    public StringFilter getKey() {
        return key;
    }

    public StringFilter key() {
        if (key == null) {
            key = new StringFilter();
        }
        return key;
    }

    public void setKey(StringFilter key) {
        this.key = key;
    }

    public LongFilter getUpdatePreconditionId() {
        return updatePreconditionId;
    }

    public LongFilter updatePreconditionId() {
        if (updatePreconditionId == null) {
            updatePreconditionId = new LongFilter();
        }
        return updatePreconditionId;
    }

    public void setUpdatePreconditionId(LongFilter updatePreconditionId) {
        this.updatePreconditionId = updatePreconditionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UpdateKeysCriteria that = (UpdateKeysCriteria) o;
        return (
            Objects.equals(id, that.id) && Objects.equals(key, that.key) && Objects.equals(updatePreconditionId, that.updatePreconditionId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key, updatePreconditionId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UpdateKeysCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (key != null ? "key=" + key + ", " : "") +
            (updatePreconditionId != null ? "updatePreconditionId=" + updatePreconditionId + ", " : "") +
            "}";
    }
}
