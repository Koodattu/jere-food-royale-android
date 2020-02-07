package fi.foodroyale.raccis.jsonpojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Course {

    @SerializedName("title_fi")
    @Expose
    private String titleFi;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("title_en")
    @Expose
    private String titleEn;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("properties")
    @Expose
    private String properties;

    public String getTitleFi() {
        return titleFi;
    }

    public void setTitleFi(String titleFi) {
        this.titleFi = titleFi;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

}