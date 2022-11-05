package br.ufpb.dcx.appalpha.control.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiURLRetrofit
{
    @GET("url_api.json")
    Call<ApiURL> getApiURL();
}
