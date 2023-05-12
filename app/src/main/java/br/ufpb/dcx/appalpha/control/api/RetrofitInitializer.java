package br.ufpb.dcx.appalpha.control.api;

import java.util.concurrent.TimeUnit;

import br.ufpb.dcx.appalpha.control.service.interfaces.ChallengeApiService;
import br.ufpb.dcx.appalpha.control.service.interfaces.ThemesApiServiceInterface;
import br.ufpb.dcx.appalpha.view.activities.MainActivity;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class to configure retrofit with API domain
 */
public class RetrofitInitializer {

    /**
     * Set up base domain API
     */
    public static String BASE_URL = ApiConfig.getInstance(MainActivity.getMainContext()).getDominio();

    /**
     * Alloc retrofit
     */
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(getOkHttpClient()).build();

    /**
     * Configure request timeout in API calls
     *
     * @return
     */
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

    /**
     * Service base for Context for retrofit calls
     *
     * @return
     */
    public ThemesApiServiceInterface contextService() {
        return retrofit.create(ThemesApiServiceInterface.class);
    }

    /**
     * Service base for Challenge for retrofit calls
     *
     * @return
     */
    public ChallengeApiService challengeService() {
        return retrofit.create(ChallengeApiService.class);
    }

}