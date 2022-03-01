package lwi.vision.domain;

import java.util.ArrayList;
import java.util.List;

public class SearchUpdateRequest {

    private String serial;

    private String version;

    private String firmware;

    private String software;

    private List<String> updateKeys = new ArrayList<>();

    public SearchUpdateRequest() {}

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public List<String> getUpdateKeys() {
        return updateKeys;
    }

    public void setUpdateKeys(List<String> updateKeys) {
        this.updateKeys = updateKeys;
    }

    @Override
    public String toString() {
        return (
            "SearchUpdateRequest{" +
            "serial='" +
            serial +
            '\'' +
            ", firmware='" +
            firmware +
            '\'' +
            ", software='" +
            software +
            '\'' +
            ", updateKeys=" +
            updateKeys +
            '}'
        );
    }
}
