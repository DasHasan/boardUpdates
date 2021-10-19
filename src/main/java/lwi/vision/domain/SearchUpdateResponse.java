package lwi.vision.domain;

import java.util.ArrayList;
import java.util.List;

public class SearchUpdateResponse {

    private String version;

    private String mandatory;

    private String path;

    private List<String> updateKeys = new ArrayList<>();

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getUpdateKeys() {
        return updateKeys;
    }

    public void setUpdateKeys(List<String> updateKeys) {
        this.updateKeys = updateKeys;
    }
}
