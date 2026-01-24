package com.example.parquedeestacionamento.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OverpassService {

    private static final String BASE_URL = "https://overpass-api.de/api/";

    private static volatile OverpassApi API;

    public static OverpassApi api() {
        if (API == null) {
            synchronized (OverpassService.class) {
                if (API == null) {
                    Retrofit r = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    API = r.create(OverpassApi.class);
                }
            }
        }
        return API;
    }
}
