package fi.foodroyale.raccis.jsonpojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("ref_title")
    @Expose
    private String refTitle;
    @SerializedName("ref_url")
    @Expose
    private String refUrl;
    @SerializedName("generated_timestamp")
    @Expose
    private Integer generatedTimestamp;

    public String getRefTitle() {
        return refTitle;
    }

    public void setRefTitle(String refTitle) {
        this.refTitle = refTitle;
    }

    public String getRefUrl() {
        return refUrl;
    }

    public void setRefUrl(String refUrl) {
        this.refUrl = refUrl;
    }

    public Integer getGeneratedTimestamp() {
        return generatedTimestamp;
    }

    public void setGeneratedTimestamp(Integer generatedTimestamp) {
        this.generatedTimestamp = generatedTimestamp;
    }

}