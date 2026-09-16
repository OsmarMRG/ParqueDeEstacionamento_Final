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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Locale;

/**
 * Fragment que exibe mapa com parques de estacionamento públicos.
 * Utiliza a API Overpass (OpenStreetMap) para obter localizações.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private boolean isMapReady = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeMap();
    }

    /**
     * Inicializa o fragment do mapa.
     */
    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        isMapReady = true;

        // Centrar no ponto padrão
        LatLng defaultLocation = new LatLng(
                Constants.MAP_DEFAULT_LATITUDE,
                Constants.MAP_DEFAULT_LONGITUDE);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                defaultLocation,
                Constants.MAP_DEFAULT_ZOOM));

        // Carregar parques de estacionamento
        fetchNearbyParkings(defaultLocation.latitude, defaultLocation.longitude);
    }

    /**
     * Busca parques de estacionamento próximos via API Overpass.
     *
     * @param latitude  Latitude central da busca
     * @param longitude Longitude central da busca
     */
    private void fetchNearbyParkings(double latitude, double longitude) {
        if (!isMapReady || googleMap == null)
            return;

        // Query Overpass QL para buscar parques de estacionamento
        String query = String.format(Locale.US,
                "[out:json];node[amenity=parking](around:%d,%f,%f);out;",
                Constants.MAP_SEARCH_RADIUS_METERS,
                latitude,
                longitude);

        OverpassService.api().query(query).enqueue(new Callback<OverpassResponse>() {
            @Override
            public void onResponse(@NonNull Call<OverpassResponse> call,
                    @NonNull Response<OverpassResponse> response) {
                if (!isAdded())
                    return; // Fragment já não está attached

                if (!response.isSuccessful() || response.body() == null) {
                    showToast(R.string.error_fetch_parkings);
                    return;
                }

                displayParkingMarkers(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<OverpassResponse> call,
                    @NonNull Throwable throwable) {
                if (isAdded()) {
                    showToast(R.string.error_network);
                }
            }
        });
    }

    /**
     * Adiciona marcadores no mapa para cada parque encontrado.
     *
     * @param response Resposta da API Overpass
     */
    private void displayParkingMarkers(@NonNull OverpassResponse response) {
        if (googleMap == null || response.elements == null)
            return;

        int count = 0;
        String defaultName = getString(R.string.default_parking_name);

        for (OverpassResponse.Element element : response.elements) {
            LatLng position = new LatLng(element.lat, element.lon);

            // Obter nome do parque ou usar nome padrão
            String name = (element.tags != null && element.tags.get("name") != null)
                    ? element.tags.get("name")
                    : defaultName;

            googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(name));

            count++;

            // Limitar número de marcadores por performance
            if (count >= Constants.MAP_MAX_MARKERS)
                break;
        }

        showToast(getString(R.string.parkings_found, count));
    }

    /**
     * Mostra mensagem Toast de forma segura.
     */
    private void showToast(int messageResId) {
        if (isAdded() && getContext() != null) {
            Toast.makeText(getContext(), messageResId, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Mostra mensagem Toast de forma segura.
     */
    private void showToast(@NonNull String message) {
        if (isAdded() && getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
