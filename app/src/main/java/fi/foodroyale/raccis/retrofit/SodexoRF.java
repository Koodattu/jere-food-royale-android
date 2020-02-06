package fi.foodroyale.raccis.retrofit;

import fi.foodroyale.raccis.gsonclasses.SodexoGson;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Jupe Danger on 21.3.2018.
 */

public interface SodexoRF {
    @GET("/ruokalistat/output/daily_json/{rid}/{year}/{month}/{day}/fi")
    Call<SodexoGson> getCoursesSodexo(@Path("rid") String restaurantCode, @Path("year") String year, @Path("month") String month, @Path("day") String day);
}
