package lwi.vision.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Board.
 */
@Entity
@Table(name = "board")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Board implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "serial")
    private String serial;

    @OneToMany(mappedBy = "board")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "board" }, allowSetters = true)
    private Set<Software> software = new HashSet<>();

    @OneToMany(mappedBy = "board")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "board" }, allowSetters = true)
    private Set<Firmware> firmware = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Board id(Long id) {
        this.id = id;
        return this;
    }

    public String getSerial() {
        return this.serial;
    }

    public Board serial(String serial) {
        this.serial = serial;
        return this;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Set<Software> getSoftware() {
        return this.software;
    }

    public Board software(Set<Software> software) {
        this.setSoftware(software);
        return this;
    }

    public Board addSoftware(Software software) {
        this.software.add(software);
        software.setBoard(this);
        return this;
    }

    public Board removeSoftware(Software software) {
        this.software.remove(software);
        software.setBoard(null);
        return this;
    }

    public void setSoftware(Set<Software> software) {
        if (this.software != null) {
            this.software.forEach(i -> i.setBoard(null));
        }
        if (software != null) {
            software.forEach(i -> i.setBoard(this));
        }
        this.software = software;
    }

    public Set<Firmware> getFirmware() {
        return this.firmware;
    }

    public Board firmware(Set<Firmware> firmware) {
        this.setFirmware(firmware);
        return this;
    }

    public Board addFirmware(Firmware firmware) {
        this.firmware.add(firmware);
        firmware.setBoard(this);
        return this;
    }

    public Board removeFirmware(Firmware firmware) {
        this.firmware.remove(firmware);
        firmware.setBoard(null);
        return this;
    }

    public void setFirmware(Set<Firmware> firmware) {
        if (this.firmware != null) {
            this.firmware.forEach(i -> i.setBoard(null));
        }
        if (firmware != null) {
            firmware.forEach(i -> i.setBoard(this));
        }
        this.firmware = firmware;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Board)) {
            return false;
        }
        return id != null && id.equals(((Board) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Board{" +
            "id=" + getId() +
            ", serial='" + getSerial() + "'" +
            "}";
    }
}
