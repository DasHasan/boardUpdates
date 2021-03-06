package lwi.vision.domain;

import java.util.ArrayList;
import java.util.List;
import lwi.vision.domain.enumeration.UpdateType;

public class SearchUpdateResponse {

    private String version;

    private String mandatory;

    private List<String> updateKeys = new ArrayList<>();

    private String downloadUrl;

    private String status;

    private UpdateType updateType;

    public SearchUpdateResponse() {}

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public List<String> getUpdateKeys() {
        return updateKeys;
    }

    public void setUpdateKeys(List<String> updateKeys) {
        this.updateKeys = updateKeys;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }
}
