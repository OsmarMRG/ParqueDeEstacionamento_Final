package com.example.parquedeestacionamento.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.parquedeestacionamento.R;
import com.example.parquedeestacionamento.data.remote.OverpassService;
import com.example.parquedeestacionamento.model.OverpassResponse;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        SupportMapFragment smf = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (smf != null) smf.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        // Ponto inicial (Lisboa). muda se quiseres
        LatLng lisboa = new LatLng(38.7223, -9.1393);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lisboa, 13f));

        fetchParkings(lisboa.latitude, lisboa.longitude);
    }

    private void fetchParkings(double lat, double lon) {
        // Overpass QL: buscar amenity=parking num raio de 2000m
        String q =
                "[out:json];" +
                        "node[amenity=parking](around:2000," + lat + "," + lon + ");" +
                        "out;";

        OverpassService.api().query(q).enqueue(new Callback<OverpassResponse>() {
            @Override
            public void onResponse(@NonNull Call<OverpassResponse> call, @NonNull Response<OverpassResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(getContext(), "Falha a buscar parques", Toast.LENGTH_SHORT).show();
                    return;
                }

                int count = 0;
                for (OverpassResponse.Element e : response.body().elements) {
                    LatLng p = new LatLng(e.lat, e.lon);
                    String name = (e.tags != null && e.tags.get("name") != null) ? e.tags.get("name") : "Parque";
                    map.addMarker(new MarkerOptions().position(p).title(name));
                    count++;
                    if (count > 60) break; // não vamos encher o mapa até explodir
                }

                Toast.makeText(getContext(), "Parques encontrados: " + count, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<OverpassResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Sem net ou Overpass zangado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
