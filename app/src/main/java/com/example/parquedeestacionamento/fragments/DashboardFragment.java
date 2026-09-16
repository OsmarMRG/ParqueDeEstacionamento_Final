package com.example.parquedeestacionamento.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.parquedeestacionamento.R;
import com.example.parquedeestacionamento.data.local.dao.ParkingDao;
import com.example.parquedeestacionamento.data.local.db.AppDatabase;
import com.example.parquedeestacionamento.data.local.entities.ParkingEntity;
import com.example.parquedeestacionamento.utils.AppExecutors;
import com.example.parquedeestacionamento.utils.Constants;

import java.util.regex.Pattern;

// Dashboard: regista entradas e saídas de veículos e mostra quantos estão dentro
public class DashboardFragment extends Fragment {

    private TextView tvInsideCount;
    private EditText etPlate;
    private ParkingDao parkingDao;
    private final Pattern platePattern = Pattern.compile(Constants.PLATE_REGEX);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parkingDao = AppDatabase.getInstance(requireContext()).parkingDao();
        tvInsideCount = view.findViewById(R.id.tvInside);
        etPlate = view.findViewById(R.id.etPlate);
        view.findViewById(R.id.btnEntry).setOnClickListener(v -> handleEntry());
        view.findViewById(R.id.btnExit).setOnClickListener(v -> handleExit());
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshInsideCount();
    }

    // Atualiza o contador de veículos dentro do parque
    private void refreshInsideCount() {
        AppExecutors.io().execute(() -> {
            int count = parkingDao.countInside();
            AppExecutors.main(() -> tvInsideCount.setText(String.valueOf(count)));
        });
    }

    // Regista a entrada de um veículo
    private void handleEntry() {
        String plate = getValidPlate();
        if (plate == null) return;

        AppExecutors.io().execute(() -> {
            if (parkingDao.getInsideByPlate(plate) != null) {
                toast(R.string.error_plate_already_inside);
                return;
            }
            parkingDao.insert(new ParkingEntity(plate, System.currentTimeMillis()));
            AppExecutors.main(() -> {
                toast(R.string.success_entry);
                etPlate.setText("");
                refreshInsideCount();
            });
        });
    }

    // Regista a saída de um veículo
    private void handleExit() {
        String plate = getValidPlate();
        if (plate == null) return;

        AppExecutors.io().execute(() -> {
            ParkingEntity existing = parkingDao.getInsideByPlate(plate);
            if (existing == null) {
                toast(R.string.error_plate_not_inside);
                return;
            }
            existing.exitTime = System.currentTimeMillis();
            existing.isInside = false;
            parkingDao.update(existing);
            AppExecutors.main(() -> {
                toast(R.string.success_exit);
                etPlate.setText("");
                refreshInsideCount();
            });
        });
    }

    // Lê e valida a matrícula; retorna null se inválida
    @Nullable
    private String getValidPlate() {
        String plate = etPlate.getText().toString().trim().toUpperCase();
        if (plate.isEmpty()) {
            toast(R.string.error_empty_plate);
            return null;
        }
        if (!platePattern.matcher(plate).matches()) {
            toast(R.string.error_invalid_plate_format);
            return null;
        }
        return plate;
    }

    // Mostra um Toast de forma segura
    private void toast(int resId) {
        if (getContext() != null)
            Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }
}
