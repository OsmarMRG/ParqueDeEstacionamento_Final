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
import com.example.parquedeestacionamento.utils.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Mapa: mostra parques de estacionamento próximos usando a API Overpass do OpenStreetMap
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private boolean isMapReady = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        isMapReady = true;

        // Centrar no Beja e procurar parques próximos
        LatLng location = new LatLng(Constants.MAP_DEFAULT_LATITUDE, Constants.MAP_DEFAULT_LONGITUDE);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Constants.MAP_DEFAULT_ZOOM));
        fetchParkings(location.latitude, location.longitude);
    }

    // Query Overpass para obter parques de estacionamento numa área
    private void fetchParkings(double lat, double lon) {
        String query = String.format(Locale.US,
                "[out:json];node[amenity=parking](around:%d,%f,%f);out;",
                Constants.MAP_SEARCH_RADIUS_METERS, lat, lon);

        OverpassService.api().query(query).enqueue(new Callback<OverpassResponse>() {
            @Override
            public void onResponse(@NonNull Call<OverpassResponse> call, @NonNull Response<OverpassResponse> response) {
                if (!isAdded() || !response.isSuccessful() || response.body() == null) return;
                showMarkers(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<OverpassResponse> call, @NonNull Throwable t) {
                if (isAdded()) toast(R.string.error_network);
            }
        });
    }

    // Adiciona marcadores no mapa para cada parque encontrado
    private void showMarkers(@NonNull OverpassResponse response) {
        if (googleMap == null || response.elements == null) return;

        int count = 0;
        String defaultName = getString(R.string.default_parking_name);

        for (OverpassResponse.Element e : response.elements) {
            String name = (e.tags != null && e.tags.get("name") != null) ? e.tags.get("name") : defaultName;
            googleMap.addMarker(new MarkerOptions().position(new LatLng(e.lat, e.lon)).title(name));
            if (++count >= Constants.MAP_MAX_MARKERS) break;
        }

        toast(getString(R.string.parkings_found, count));
    }

    private void toast(String msg) {
        if (isAdded() && getContext() != null)
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void toast(int resId) {
        if (isAdded() && getContext() != null)
            Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }
}
