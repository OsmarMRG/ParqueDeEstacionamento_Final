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

/**
 * Fragment principal do dashboard.
 * Permite registar entradas e saídas de veículos no parque.
 */
public class DashboardFragment extends Fragment {

    private TextView tvInsideCount;
    private EditText etPlate;
    private ParkingDao parkingDao;

    // Padrão para validação de matrículas portuguesas
    private final Pattern platePattern = Pattern.compile(Constants.PLATE_REGEX);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeDatabase();
        initializeViews(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshInsideCount();
    }

    /**
     * Inicializa referência à base de dados.
     */
    private void initializeDatabase() {
        AppDatabase database = AppDatabase.getInstance(requireContext());
        parkingDao = database.parkingDao();
    }

    /**
     * Inicializa views e configura listeners.
     */
    private void initializeViews(@NonNull View view) {
        tvInsideCount = view.findViewById(R.id.tvInside);
        etPlate = view.findViewById(R.id.etPlate);

        view.findViewById(R.id.btnEntry).setOnClickListener(v -> handleEntry());
        view.findViewById(R.id.btnExit).setOnClickListener(v -> handleExit());
    }

    /**
     * Atualiza o contador de veículos dentro do parque.
     */
    private void refreshInsideCount() {
        AppExecutors.io().execute(() -> {
            int count = parkingDao.countInside();

            runOnUiThreadSafely(() -> {
                // Mostrar apenas o número (novo design do dashboard)
                tvInsideCount.setText(String.valueOf(count));
            });
        });
    }

    /**
     * Processa uma entrada de veículo no parque.
     */
    private void handleEntry() {
        String plate = getAndValidatePlate();
        if (plate == null)
            return;

        AppExecutors.io().execute(() -> {
            // Verificar se já está dentro
            ParkingEntity existing = parkingDao.getInsideByPlate(plate);

            if (existing != null) {
                showToastSafely(R.string.error_plate_already_inside);
                return;
            }

            // Registar entrada
            ParkingEntity newEntry = new ParkingEntity(plate, System.currentTimeMillis());
            parkingDao.insert(newEntry);

            runOnUiThreadSafely(() -> {
                showToast(R.string.success_entry);
                etPlate.setText("");
                refreshInsideCount();
            });
        });
    }

    /**
     * Processa uma saída de veículo do parque.
     */
    private void handleExit() {
        String plate = getAndValidatePlate();
        if (plate == null)
            return;

        AppExecutors.io().execute(() -> {
            // Verificar se está dentro
            ParkingEntity existing = parkingDao.getInsideByPlate(plate);

            if (existing == null) {
                showToastSafely(R.string.error_plate_not_inside);
                return;
            }

            // Registar saída
            existing.exitTime = System.currentTimeMillis();
            existing.isInside = false;
            parkingDao.update(existing);

            runOnUiThreadSafely(() -> {
                showToast(R.string.success_exit);
                etPlate.setText("");
                refreshInsideCount();
            });
        });
    }

    /**
     * Obtém e valida a matrícula introduzida.
     *
     * @return Matrícula em maiúsculas ou null se inválida
     */
    @Nullable
    private String getAndValidatePlate() {
        String plate = etPlate.getText().toString().trim().toUpperCase();

        if (plate.isEmpty()) {
            showToast(R.string.error_empty_plate);
            return null;
        }

        // Validar formato de matrícula portuguesa
        if (!platePattern.matcher(plate).matches()) {
            showToast(R.string.error_invalid_plate_format);
            return null;
        }

        return plate;
    }

    /**
     * Executa código na UI thread de forma segura.
     */
    private void runOnUiThreadSafely(@NonNull Runnable action) {
        if (isAdded() && getActivity() != null) {
            requireActivity().runOnUiThread(action);
        }
    }

    /**
     * Mostra Toast de forma segura a partir de background thread.
     */
    private void showToastSafely(int messageResId) {
        runOnUiThreadSafely(() -> showToast(messageResId));
    }

    /**
     * Mostra uma mensagem Toast.
     */
    private void showToast(int messageResId) {
        if (getContext() != null) {
            Toast.makeText(getContext(), messageResId, Toast.LENGTH_SHORT).show();
        }
    }
}
