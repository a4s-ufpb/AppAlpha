package br.ufpb.dcx.appalpha.control.service;

import android.util.Log;

import java.util.List;

import br.ufpb.dcx.appalpha.control.api.RetrofitInitializer;
import br.ufpb.dcx.appalpha.model.bean.Theme;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class to communicate to the API with Retrofit for get Theme
 */
public class ThemesApiService {
    private static final String TAG = "ThemesApiService";
    private List<Theme> themes;
    private Theme themeResult;
    private static ThemesApiService instance;

    private ThemesApiService() {
    }

    /**
     * Initialize the shared instance
     *
     * @return
     */
    public static ThemesApiService getInstance() {
        if (instance == null) {
            instance = new ThemesApiService();
        }
        return instance;
    }

    /**
     * Get an Theme from API with theme Id
     *
     * @param id
     * @return
     */
    public Theme find(int id) {
        themeResult = null;
        Call call = new RetrofitInitializer().contextService().find(id);
        call.enqueue(new Callback<Theme>() {
            @Override
            public void onResponse(Call<Theme> call, Response<Theme> response) {
                if (response != null) {
                    themeResult = response.body();
                }
            }

            @Override
            public void onFailure(Call<Theme> call, Throwable t) {
                Log.e(TAG, "Erro ao recuperar tema: " + t.getMessage());
            }
        });

        return themeResult;
    }

}
