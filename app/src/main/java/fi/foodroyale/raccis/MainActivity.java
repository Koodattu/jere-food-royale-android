package fi.foodroyale.raccis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import fi.foodroyale.raccis.gsonclasses.Course;
import fi.foodroyale.raccis.gsonclasses.SodexoGson;
import fi.foodroyale.raccis.objects.Dish;
import fi.foodroyale.raccis.retrofit.EventtiRF;
import fi.foodroyale.raccis.retrofit.SodexoRF;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_sodexo) RecyclerView rvSodexo;
    @BindView(R.id.rv_eventti) RecyclerView rvEventti;
    @BindView(R.id.day) TextView date;
    @BindView(R.id.pb_sodexo) ProgressBar pbSodexo;
    @BindView(R.id.pb_eventti) ProgressBar pbEventti;
    @BindView(R.id.error_sodexo) TextView errorSodexo;
    @BindView(R.id.error_eventti) TextView errorEventti;
    @BindView(R.id.toolbar) Toolbar toolbar;

    List<IFlexible> sodexoCourses = new ArrayList<>();
    List<IFlexible> eventtiCourses = new ArrayList<>();

    FlexibleAdapter<IFlexible> sodexoAdapter;
    FlexibleAdapter<IFlexible> eventtiAdapter;

    boolean canFetchSodexo = true;
    boolean canFetchEventti = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setTitle("Jere Känsälä's SeAMK Food Royale");

        rvSodexo.setLayoutManager(new LinearLayoutManager(this));
        rvEventti.setLayoutManager(new LinearLayoutManager(this));
    }

    // TODO
    //JSONObject songsObject = json.getJSONObject("songs");
    //JSONArray songsArray = songsObject.toJSONArray(songsObject.names());

    void setTitle(){

        Calendar calendar = Calendar.getInstance();

        int dow = calendar.get(Calendar.DAY_OF_WEEK);
        String day = "";

        switch (dow){
            case 2:
                day = "Maanantai";
                break;
            case 3:
                day = "Tiistai";
                break;
            case 4:
                day = "Keskiviikko";
                break;
            case 5:
                day = "Torstai";
                break;
            case 6:
                day = "Perjantai";
                break;
            case 7:
                day = "Lauantai";
                break;
            case 1:
                day = "Sunnuntai";
                break;
        }

        DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        date.setText(day + " " + format.format(calendar.getTime()));

    }

    @Override
    public void onStart(){
        super.onStart();
        setTitle();
        fetchSodexo("873", 0);
        fetchEventti();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            if (canFetchEventti) {
                fetchEventti();
            }
            if (canFetchSodexo) {
                fetchSodexo("873", 0);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void updateEventti(int state){
        //0 = haku alkaa
        //1 = huono päivä
        //2 = aikakatkaisu
        //3 = internet yhteys puuttuu
        //4 = sivuston rakenne

        if (eventtiCourses.size() == 0){
            switch (state){
                case 0:
                    rvEventti.setVisibility(View.GONE);
                    pbEventti.setVisibility(View.VISIBLE);
                    errorEventti.setVisibility(View.GONE);
                    errorEventti.setText("");
                    break;
                case 1:
                    rvEventti.setVisibility(View.GONE);
                    pbEventti.setVisibility(View.GONE);
                    errorEventti.setVisibility(View.VISIBLE);
                    errorEventti.setText("Ruokalistaa ei ole tälle päivälle saatavilla.");
                    break;
                case 2:
                    rvEventti.setVisibility(View.GONE);
                    pbEventti.setVisibility(View.GONE);
                    errorEventti.setVisibility(View.VISIBLE);
                    errorEventti.setText("Palvelin ei vastannut ajoissa.");
                    break;
                case 3:
                    rvEventti.setVisibility(View.GONE);
                    pbEventti.setVisibility(View.GONE);
                    errorEventti.setVisibility(View.VISIBLE);
                    errorEventti.setText("Internet yhteyttä ei ole.");
                    break;
                case 4:
                    rvEventti.setVisibility(View.GONE);
                    pbEventti.setVisibility(View.GONE);
                    errorEventti.setVisibility(View.VISIBLE);
                    errorEventti.setText("Sivuston rakenne on muuttunut, sovellus täytyy päivittää.");
                    break;
                default:
                    rvEventti.setVisibility(View.GONE);
                    pbEventti.setVisibility(View.GONE);
                    errorEventti.setVisibility(View.VISIBLE);
                    errorEventti.setText("Tapahtui tuntematon virhe.");
                    break;
            }
        } else {
            rvEventti.setVisibility(View.VISIBLE);
            pbEventti.setVisibility(View.GONE);
            errorEventti.setVisibility(View.GONE);
        }
    }

    void updateSodexo(int state){
        //0 = haku alkaa
        //1 = huono päivä
        //2 = aikakatkaisu
        //3 = internet yhteys puuttuu

        if (sodexoCourses.size() == 0){
            switch (state){
                case 0:
                    rvSodexo.setVisibility(View.GONE);
                    pbSodexo.setVisibility(View.VISIBLE);
                    errorSodexo.setVisibility(View.GONE);
                    errorSodexo.setText("");
                    break;
                case 1:
                    rvSodexo.setVisibility(View.GONE);
                    pbSodexo.setVisibility(View.GONE);
                    errorSodexo.setVisibility(View.VISIBLE);
                    errorSodexo.setText("Ruokalistaa ei ole tälle päivälle saatavilla.");
                    break;
                case 2:
                    rvSodexo.setVisibility(View.GONE);
                    pbSodexo.setVisibility(View.GONE);
                    errorSodexo.setVisibility(View.VISIBLE);
                    errorSodexo.setText("Palvelin ei vastannut ajoissa.");
                    break;
                case 3:
                    rvSodexo.setVisibility(View.GONE);
                    pbSodexo.setVisibility(View.GONE);
                    errorSodexo.setVisibility(View.VISIBLE);
                    errorSodexo.setText("Internet yhteyttä ei ole.");
                    break;
                default:
                    rvSodexo.setVisibility(View.GONE);
                    pbSodexo.setVisibility(View.GONE);
                    errorSodexo.setVisibility(View.VISIBLE);
                    errorSodexo.setText("Tapahtui tuntematon virhe.");
                    break;
            }
        } else {
            rvSodexo.setVisibility(View.VISIBLE);
            pbSodexo.setVisibility(View.GONE);
            errorSodexo.setVisibility(View.GONE);
        }
    }

    void fetchSodexo(String restaurantCode, int day) {
        sodexoCourses = new ArrayList<>();
        updateSodexo(0);
        canFetchSodexo = false;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, day);

        OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://www.sodexo.fi").addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient).build();

        SodexoRF sodexoRF =  retrofit.create(SodexoRF.class);
        Call<SodexoGson> sodexoGsonCall = sodexoRF.getCoursesSodexo(restaurantCode, Integer.toString(calendar.get(Calendar.YEAR)), (calendar.get(Calendar.MONTH) + 1) < 10 ? ("0" + Integer.toString(calendar.get(Calendar.MONTH) + 1)) : Integer.toString((calendar.get(Calendar.MONTH) + 1)), calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)) : Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
        sodexoGsonCall.enqueue(new Callback<SodexoGson>() {
            @Override
            public void onResponse(@NonNull Call<SodexoGson> call, @NonNull Response<SodexoGson> response) {
                boolean goodDay = false;
                Calendar calendar = Calendar.getInstance();

                int dow = calendar.get(Calendar.DAY_OF_WEEK);

                switch (dow){
                    case 2:
                        goodDay = true;
                        break;
                    case 3:
                        goodDay = true;
                        break;
                    case 4:
                        goodDay = true;
                        break;
                    case 5:
                        goodDay = true;
                        break;
                    case 6:
                        goodDay = true;
                        break;
                }

                if (goodDay) {
                    SodexoGson sodexoGson = response.body();
                    for (Course course : sodexoGson.getCourses()) {
                        sodexoCourses.add(new Dish(course.getTitleFi(), course.getPrice().substring(0, 4) + "€", course.getProperties()));
                    }
                    sodexoAdapter = new FlexibleAdapter<>(sodexoCourses);
                    rvSodexo.setAdapter(sodexoAdapter);
                    updateSodexo(-1);
                } else {
                    updateSodexo(1);
                }
                canFetchSodexo = true;
            }
            @Override
            public void onFailure(@NonNull Call<SodexoGson> call, @NonNull Throwable throwable) {
                throwable.printStackTrace();
                if (throwable.toString().contains("SocketTimeoutException")){
                    updateSodexo(2);
                } else if (throwable.toString().contains("UnknownHostException")){
                    updateSodexo(3);
                } else if (throwable.toString().contains("JsonSyntaxException")){
                    updateSodexo(1);
                } else {
                    updateSodexo(-1);
                }
                canFetchSodexo = true;
            }
        });
    }

    void fetchEventti() {
        eventtiCourses = new ArrayList<>();
        updateEventti(0);
        canFetchEventti = false;

        OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("http://www.seinajokiareena.fi/").addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient).build();

        EventtiRF eventtiRF =  retrofit.create(EventtiRF.class);
        Call<ResponseBody> elasticReservationCall = eventtiRF.getCoursesEventti();
        elasticReservationCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    String dayToday = "";
                    String dayTomorrow = "";
                    boolean goodDay = false;
                    Calendar calendar = Calendar.getInstance();

                    int dow = calendar.get(Calendar.DAY_OF_WEEK);

                    switch (dow){
                        case 2:
                            dayToday = "Ma";
                            dayTomorrow = "Ti";
                            goodDay = true;
                            break;
                        case 3:
                            dayToday = "Ti";
                            dayTomorrow = "Ke";
                            goodDay = true;
                            break;
                        case 4:
                            dayToday = "Ke";
                            dayTomorrow = "To";
                            goodDay = true;
                            break;
                        case 5:
                            dayToday = "To";
                            dayTomorrow = "Pe";
                            goodDay = true;
                            break;
                        case 6:
                            dayToday = "Pe";
                            dayTomorrow = "La";
                            goodDay = true;
                            break;
                        case 7:
                            dayToday = "La";
                            dayTomorrow = "Su";
                            break;
                        case 1:
                            dayToday = "Su";
                            dayTomorrow = "Ma";
                            break;
                    }

                    if (goodDay) {


                        try {
                            DateFormat format = new SimpleDateFormat("d.M.", Locale.US);
                            calendar.setFirstDayOfWeek(Calendar.MONDAY);
                            String today = format.format(calendar.getTime());
                            calendar.add(Calendar.DAY_OF_YEAR, 1);
                            String tomorrow = format.format(calendar.getTime());

                            calendar = Calendar.getInstance();
                            calendar.set(Calendar.DAY_OF_WEEK, 7);
                            String saturday = format.format(calendar.getTime());

                            String body = response.body().string();
                            body = body.substring(body.indexOf("<h2>Ravintola Eventti (B-halli)</h2>"));
                            body = body.substring(0, body.indexOf("<h4>La " + saturday + "</h4>") + ("<h4>La " + saturday + "</h4>").length());
                            body = body.substring(body.indexOf("<h4>" + dayToday + " " + today + "</h4>"));
                            body = body.substring(0, body.indexOf("<h4>" + dayTomorrow + " " + tomorrow + "</h4>"));

                            body = body.replace("<h4>" + dayToday + " " + today + "</h4>", "");
                            body = body.replace("<p>&nbsp;</p>", "");

                            String[] split = body.split("<br />");

                            StringBuilder tmpString = new StringBuilder();

                            for (String aSplit : split) {
                                tmpString.append(aSplit).append("\n");
                            }

                            split = tmpString.toString().split("\n");

                            int breakPoint = 0;
                            int left = 0;

                            List<String> normalCourses = new ArrayList<>();

                            for (int i = 0; i < split.length; i++) {
                                if (split[i].contains("Lis&auml;hinnalla")) {
                                    breakPoint = i;
                                    left = split.length - i;
                                    break;
                                }
                                normalCourses.add(split[i]);
                            }

                            String remainder = "";

                            for (int i = 0; i < left; i++) {
                                remainder += split[i + breakPoint] + "\n";
                            }

                            String[] splitRemainder = remainder.split("\n");

                            List<String> extraCourses = Arrays.asList(splitRemainder);

                            for (String s : normalCourses) {
                                s = s.replace("<p>Ravintola avoinna klo 9.00 - 14.00, lounas klo 10.30 - 13.30</p>", "");
                                s = s.replace("\n", "");
                                s = s.replace("<p>", "");
                                s = s.replace("</p>", "");
                                s = s.replace("&nbsp;", "");
                                s = s.replace("Lis&auml;hinnalla:", "");
                                s = s.replace("&auml;", "ä");
                                s = s.replace("&ouml;", "ö");

                                String name = "";
                                String props = "";

                                if (s.startsWith("(")){
                                    ((Dish)eventtiCourses.get(eventtiCourses.size() - 1)).setDesc(s);
                                } else {
                                    if (s.matches(".*[a-zA-Z]+.*")) {
                                        int index = 0;
                                        for (int j = 6; j < s.length(); j++) {
                                            int k = 0;
                                            if (Character.isUpperCase(s.charAt(j)) || s.charAt(j) == '*') {
                                                while (Character.isLetter(s.charAt(j + k))) {
                                                    k++;
                                                    if ((j + k) == s.length()){
                                                        break;
                                                    }
                                                }
                                                if (k < 4) {
                                                    index = j - 1;
                                                    break;
                                                }
                                            }
                                        }
                                        if (index != 0) {
                                            name = s.substring(0, index);
                                            props = s.substring(index);
                                        } else {
                                            name = s.substring(0, s.length());
                                        }
                                    }
                                }

                                if (!name.isEmpty()) {
                                    eventtiCourses.add(new Dish(name, "2,60€", props));
                                }
                            }

                            for (String s : extraCourses) {
                                s = s.replace("\n", "");
                                s = s.replace("<p>", "");
                                s = s.replace("</p>", "");
                                s = s.replace("&nbsp;", "");
                                s = s.replace("Lis&auml;hinnalla:", "");
                                s = s.replace("&auml;", "ä");
                                s = s.replace("&ouml;", "ö");

                                String name = "";
                                String props = "";

                                if (s.matches(".*[a-zA-Z]+.*")) {
                                    int index = 0;
                                    for (int j = 2; j < s.length(); j++) {
                                        int k = 0;
                                        if (Character.isUpperCase(s.charAt(j)) || s.charAt(j) == '*') {
                                            while (Character.isLetter(s.charAt(j + k))) {
                                                k++;
                                                if ((j + k) == s.length()){
                                                    break;
                                                }
                                            }
                                            if (k < 4) {
                                                index = j - 1;
                                                break;
                                            }
                                        }
                                    }
                                    if (index != 0) {
                                        name = s.substring(0, index);
                                        props = s.substring(index);
                                    } else {
                                        name = s.substring(0, s.length());
                                    }
                                }

                                if (!name.isEmpty()) {
                                    eventtiCourses.add(new Dish(name, "4,95€", props));
                                }
                            }

                            eventtiAdapter = new FlexibleAdapter<>(eventtiCourses);
                            rvEventti.setAdapter(eventtiAdapter);
                            updateEventti(-1);
                        } catch (Exception e) {
                            eventtiCourses = new ArrayList<>();
                            updateEventti(4);
                        }
                    } else {
                        updateEventti(1);
                    }
                    canFetchEventti = true;
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                if (throwable.toString().contains("SocketTimeoutException")){
                    updateEventti(2);
                } else if (throwable.toString().contains("UnknownHostException")){
                    updateEventti(3);
                } else {
                    updateEventti(-1);
                }
                canFetchEventti = true;
            }
        });
    }
}
