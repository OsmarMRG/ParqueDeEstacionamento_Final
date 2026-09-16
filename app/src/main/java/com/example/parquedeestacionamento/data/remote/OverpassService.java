package com.example.parquedeestacionamento.data.remote;

import androidx.annotation.NonNull;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Cria e devolve a instância do Retrofit para a API Overpass
public final class OverpassService {

    private static final String BASE_URL = "https://overpass-api.de/api/";
    private static volatile OverpassApi INSTANCE;

    private OverpassService() {}

    @NonNull
    public static OverpassApi api() {
        if (INSTANCE == null) {
            synchronized (OverpassService.class) {
                if (INSTANCE == null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    INSTANCE = retrofit.create(OverpassApi.class);
                }
            }
        }
        return INSTANCE;
    }
}
