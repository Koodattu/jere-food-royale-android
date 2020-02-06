
package fi.foodroyale.raccis.gsonclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("generated_timestamp")
    @Expose
    private Integer generatedTimestamp;
    @SerializedName("requested_timestamp")
    @Expose
    private Integer requestedTimestamp;
    @SerializedName("ref_url")
    @Expose
    private String refUrl;
    @SerializedName("ref_title")
    @Expose
    private String refTitle;

    public Integer getGeneratedTimestamp() {
        return generatedTimestamp;
    }

    public void setGeneratedTimestamp(Integer generatedTimestamp) {
        this.generatedTimestamp = generatedTimestamp;
    }

    public Integer getRequestedTimestamp() {
        return requestedTimestamp;
    }

    public void setRequestedTimestamp(Integer requestedTimestamp) {
        this.requestedTimestamp = requestedTimestamp;
    }

    public String getRefUrl() {
        return refUrl;
    }

    public void setRefUrl(String refUrl) {
        this.refUrl = refUrl;
    }

    public String getRefTitle() {
        return refTitle;
    }

    public void setRefTitle(String refTitle) {
        this.refTitle = refTitle;
    }

}
