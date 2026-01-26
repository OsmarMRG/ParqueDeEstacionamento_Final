package com.example.parquedeestacionamento.data.remote;

import com.example.parquedeestacionamento.model.OverpassResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Interface Retrofit para a API Overpass (OpenStreetMap).
 * Permite consultar dados geográficos sobre parques de estacionamento.
 *
 * @see <a href="https://wiki.openstreetmap.org/wiki/Overpass_API">Overpass API
 *      Documentation</a>
 */
public interface OverpassApi {

    /**
     * Executa uma query Overpass QL.
     *
     * @param data Query em formato Overpass QL
     * @return Call com a resposta contendo elementos geográficos
     */
    @FormUrlEncoded
    @POST("interpreter")
    Call<OverpassResponse> query(@Field("data") String data);
}
