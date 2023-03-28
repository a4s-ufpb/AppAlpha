package br.ufpb.dcx.appalpha.control.service.interfaces;

import br.ufpb.dcx.appalpha.model.bean.Theme;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Interface to specify to Retrofit the path and type for getting Theme by Id
 */
public interface ThemesApiServiceInterface {

    @GET("v1/api/contexts/{idContext}")
    Call<Theme> find(@Path("idContext") long idContext);

}