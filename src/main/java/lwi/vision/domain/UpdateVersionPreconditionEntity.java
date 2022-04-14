package lwi.vision.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UpdateVersionPreconditionEntity.
 */
@Entity
@Table(name = "update_version_precondition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UpdateVersionPreconditionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "version")
    private String version;

    @ManyToOne
    @JsonIgnoreProperties(value = { "updateKeys", "boards" }, allowSetters = true)
    private UpdatePreconditionEntity updatePrecondition;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UpdateVersionPreconditionEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getVersion() {
        return this.version;
    }

    public UpdateVersionPreconditionEntity version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public UpdatePreconditionEntity getUpdatePrecondition() {
        return this.updatePrecondition;
    }

    public UpdateVersionPreconditionEntity updatePrecondition(UpdatePreconditionEntity updatePrecondition) {
        this.setUpdatePrecondition(updatePrecondition);
        return this;
    }

    public void setUpdatePrecondition(UpdatePreconditionEntity updatePrecondition) {
        this.updatePrecondition = updatePrecondition;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UpdateVersionPreconditionEntity)) {
            return false;
        }
        return id != null && id.equals(((UpdateVersionPreconditionEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UpdateVersionPreconditionEntity{" +
            "id=" + getId() +
            ", version='" + getVersion() + "'" +
            "}";
    }
}
