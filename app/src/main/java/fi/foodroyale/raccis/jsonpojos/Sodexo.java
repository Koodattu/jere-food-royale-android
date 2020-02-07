package fi.foodroyale.raccis.jsonpojos;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sodexo {

    @SerializedName("courses")
    @Expose
    private List<Course> courses = null;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}