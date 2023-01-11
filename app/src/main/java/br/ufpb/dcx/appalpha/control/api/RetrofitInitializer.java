package br.ufpb.dcx.appalpha.control.api;

import java.util.concurrent.TimeUnit;

import br.ufpb.dcx.appalpha.control.service.interfaces.ChallengeApiService;
import br.ufpb.dcx.appalpha.control.service.interfaces.ThemesApiServiceInterface;
import br.ufpb.dcx.appalpha.view.activities.MainActivity;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInitializer {

    public static String BASE_URL = ApiConfig.getInstance(MainActivity.getMainContext()).getDominio();

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(getOkHttpClient()).build();

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        try {

            builder.connectTimeout(30, TimeUnit.MINUTES);
            builder.readTimeout(30, TimeUnit.MINUTES);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public ThemesApiServiceInterface contextService(){
        return retrofit.create(ThemesApiServiceInterface.class);
    }

    public ChallengeApiService challengeService(){
        return retrofit.create(ChallengeApiService.class);
    }

}