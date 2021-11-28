package lwi.vision.domain;

import java.util.ArrayList;
import java.util.List;

public class SearchUpdateResponse {

    private String version;

    private String mandatory;

    private List<String> updateKeys = new ArrayList<>();

    private String downloadUrl;

    public SearchUpdateResponse(String version, String mandatory, List<String> updateKeys, String downloadUrl) {
        this.version = version;
        this.mandatory = mandatory;
        this.updateKeys = updateKeys;
        this.downloadUrl = downloadUrl;
    }

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
}
