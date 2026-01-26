package com.example.parquedeestacionamento.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.parquedeestacionamento.R;
import com.example.parquedeestacionamento.data.local.db.AppDatabase;
import com.example.parquedeestacionamento.data.local.entities.ParkingEntity;
import com.example.parquedeestacionamento.utils.AppExecutors;

public class DashboardFragment extends Fragment {

    private TextView tvInside;
    private EditText etPlate;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        tvInside = v.findViewById(R.id.tvInside);
        etPlate = v.findViewById(R.id.etPlate);
        Button btnEntry = v.findViewById(R.id.btnEntry);
        Button btnExit = v.findViewById(R.id.btnExit);

        db = AppDatabase.getInstance(requireContext());

        refreshInsideCount();

        btnEntry.setOnClickListener(x -> handleEntry());
        btnExit.setOnClickListener(x -> handleExit());
    }

    private void refreshInsideCount() {
        AppExecutors.io().execute(() -> {
            int c = db.parkingDao().countInside();
            requireActivity().runOnUiThread(() -> tvInside.setText("Dentro: " + c));
        });
    }

    private void handleEntry() {
        String plate = etPlate.getText().toString().trim().toUpperCase();
        if (plate.isEmpty()) {
            Toast.makeText(getContext(), "Escreve uma matrícula", Toast.LENGTH_SHORT).show();
            return;
        }

        AppExecutors.io().execute(() -> {
            ParkingEntity inside = db.parkingDao().getInsideByPlate(plate);
            if (inside != null) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Essa matrícula já está dentro", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            db.parkingDao().insert(new ParkingEntity(plate, System.currentTimeMillis()));
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Entrada registada", Toast.LENGTH_SHORT).show();
                etPlate.setText("");
                refreshInsideCount();
            });
        });
    }

    private void handleExit() {
        String plate = etPlate.getText().toString().trim().toUpperCase();
        if (plate.isEmpty()) {
            Toast.makeText(getContext(), "Escreve uma matrícula", Toast.LENGTH_SHORT).show();
            return;
        }

        AppExecutors.io().execute(() -> {
            ParkingEntity inside = db.parkingDao().getInsideByPlate(plate);
            if (inside == null) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Essa matrícula não está dentro", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            inside.exitTime = System.currentTimeMillis();
            inside.isInside = false;
            db.parkingDao().update(inside);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Saída registada", Toast.LENGTH_SHORT).show();
                etPlate.setText("");
                refreshInsideCount();
            });
        });
    }
}
