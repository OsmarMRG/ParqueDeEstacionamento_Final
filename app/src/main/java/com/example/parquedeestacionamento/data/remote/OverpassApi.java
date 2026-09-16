package com.example.parquedeestacionamento.data.remote;

import com.example.parquedeestacionamento.model.OverpassResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

// Interface Retrofit para a API Overpass do OpenStreetMap
public interface OverpassApi {

    // Envia uma query Overpass QL e recebe os parques de estacionamento
    @FormUrlEncoded
    @POST("interpreter")
    Call<OverpassResponse> query(@Field("data") String data);
}
