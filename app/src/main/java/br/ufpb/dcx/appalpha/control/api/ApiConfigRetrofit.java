package br.ufpb.dcx.appalpha.control.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Interface to specify path location and type in retrofit calls
 */
public interface ApiConfigRetrofit {
    @GET("config.json")
    Call<Map> getAPIConfig();
}
