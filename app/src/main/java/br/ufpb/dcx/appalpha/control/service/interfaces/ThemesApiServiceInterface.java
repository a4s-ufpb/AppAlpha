package br.ufpb.dcx.appalpha.control.service.interfaces;

import br.ufpb.dcx.appalpha.model.bean.Theme;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface ThemesApiServiceInterface {

    @GET("/educapi/v1/api/contexts/{idContext}")
    Call<Theme> find(@Path("idContext") Long idContext);

}