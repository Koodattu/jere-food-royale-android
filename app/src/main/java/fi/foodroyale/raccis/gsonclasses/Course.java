
package fi.foodroyale.raccis.gsonclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Course {

    @SerializedName("title_fi")
    @Expose
    private String titleFi;
    @SerializedName("title_en")
    @Expose
    private String titleEn;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("properties")
    @Expose
    private String properties;
    @SerializedName("desc_fi")
    @Expose
    private String descFi;
    @SerializedName("desc_en")
    @Expose
    private String descEn;
    @SerializedName("desc_se")
    @Expose
    private String descSe;
    @SerializedName("category")
    @Expose
    private String category;

    public String getTitleFi() {
        return titleFi;
    }

    public void setTitleFi(String titleFi) {
        this.titleFi = titleFi;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getDescFi() {
        return descFi;
    }

    public void setDescFi(String descFi) {
        this.descFi = descFi;
    }

    public String getDescEn() {
        return descEn;
    }

    public void setDescEn(String descEn) {
        this.descEn = descEn;
    }

    public String getDescSe() {
        return descSe;
    }

    public void setDescSe(String descSe) {
        this.descSe = descSe;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
