
package fi.foodroyale.raccis.gsonclasses;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SodexoGson {

    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("courses")
    @Expose
    private List<Course> courses = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

}
