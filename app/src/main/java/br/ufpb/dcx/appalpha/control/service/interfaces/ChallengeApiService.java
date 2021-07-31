package br.ufpb.dcx.appalpha.control.service.interfaces;

import java.util.List;

import br.ufpb.dcx.appalpha.model.bean.Challenge;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ChallengeApiService {

    @GET("/v1/api/challenges")
    Call<List<Challenge>> findAll();

}