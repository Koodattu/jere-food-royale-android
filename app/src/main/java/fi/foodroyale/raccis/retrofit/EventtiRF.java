package fi.foodroyale.raccis.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Jupe Danger on 21.3.2018.
 */

public interface EventtiRF {
    @GET("/ravintolapalvelut/lounaslista/")
    Call<ResponseBody> getCoursesEventti();
}
