package fi.foodroyale.raccis.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Jupe Danger on 21.3.2018.
 */

public interface SodexoRF {
    // https://www.sodexo.fi/ruokalistat/output/daily_json/108/2020-02-07
    @GET("/ruokalistat/output/daily_json/{rid}/{year}-{month}-{day}")
    Call<ResponseBody> getCoursesSodexo(@Path("rid") String restaurantCode, @Path("year") String year, @Path("month") String month, @Path("day") String day);
}
