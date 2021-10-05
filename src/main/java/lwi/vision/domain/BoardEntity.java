package lwi.vision.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BoardEntity.
 */
@Entity
@Table(name = "board")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BoardEntity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "serial")
    private String serial;

    @OneToMany(mappedBy = "board")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "board" }, allowSetters = true)
    private Set<FirmwareEntity> firmware = new HashSet<>();

    @OneToMany(mappedBy = "board")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "board" }, allowSetters = true)
    private Set<SoftwareEntity> software = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BoardEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getSerial() {
        return this.serial;
    }

    public BoardEntity serial(String serial) {
        this.serial = serial;
        return this;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Set<FirmwareEntity> getFirmware() {
        return this.firmware;
    }

    public BoardEntity firmware(Set<FirmwareEntity> firmware) {
        this.setFirmware(firmware);
        return this;
    }

    public BoardEntity addFirmware(FirmwareEntity firmware) {
        this.firmware.add(firmware);
        firmware.setBoard(this);
        return this;
    }

    public BoardEntity removeFirmware(FirmwareEntity firmware) {
        this.firmware.remove(firmware);
        firmware.setBoard(null);
        return this;
    }

    public void setFirmware(Set<FirmwareEntity> firmware) {
        if (this.firmware != null) {
            this.firmware.forEach(i -> i.setBoard(null));
        }
        if (firmware != null) {
            firmware.forEach(i -> i.setBoard(this));
        }
        this.firmware = firmware;
    }

    public Set<SoftwareEntity> getSoftware() {
        return this.software;
    }

    public BoardEntity software(Set<SoftwareEntity> software) {
        this.setSoftware(software);
        return this;
    }

    public BoardEntity addSoftware(SoftwareEntity software) {
        this.software.add(software);
        software.setBoard(this);
        return this;
    }

    public BoardEntity removeSoftware(SoftwareEntity software) {
        this.software.remove(software);
        software.setBoard(null);
        return this;
    }

    public void setSoftware(Set<SoftwareEntity> software) {
        if (this.software != null) {
            this.software.forEach(i -> i.setBoard(null));
        }
        if (software != null) {
            software.forEach(i -> i.setBoard(this));
        }
        this.software = software;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BoardEntity)) {
            return false;
        }
        return id != null && id.equals(((BoardEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BoardEntity{" +
            "id=" + getId() +
            ", serial='" + getSerial() + "'" +
            "}";
    }
}
