package com.example.parquedeestacionamento.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parquedeestacionamento.R;
import com.example.parquedeestacionamento.adapters.ParkingAdapter;
import com.example.parquedeestacionamento.data.local.dao.ParkingDao;
import com.example.parquedeestacionamento.data.local.db.AppDatabase;
import com.example.parquedeestacionamento.data.local.entities.ParkingEntity;
import com.example.parquedeestacionamento.utils.AppExecutors;

import java.util.List;

/**
 * Fragment que exibe a lista de registos de estacionamento.
 * Mostra histórico de entradas e saídas de veículos.
 * Permite registar novas entradas e saídas.
 */
public class ParkingListFragment extends Fragment {

    private ParkingAdapter adapter;
    private ParkingDao parkingDao;
    private LinearLayout emptyState;
    private RecyclerView recyclerView;
    private EditText etPlate;
    private Button btnEntry;
    private Button btnExit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_parking_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeDatabase();
        initializeViews(view);
        setupListeners();
    }

    /**
     * Inicializa referência à base de dados.
     */
    private void initializeDatabase() {
        AppDatabase database = AppDatabase.getInstance(requireContext());
        parkingDao = database.parkingDao();
    }

    /**
     * Inicializa views.
     */
    private void initializeViews(@NonNull View view) {
        recyclerView = view.findViewById(R.id.recyclerParking);
        emptyState = view.findViewById(R.id.emptyState);
        etPlate = view.findViewById(R.id.etPlate);
        btnEntry = view.findViewById(R.id.btnEntry);
        btnExit = view.findViewById(R.id.btnExit);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ParkingAdapter();
        recyclerView.setAdapter(adapter);
    }

    /**
     * Configura os listeners dos botões.
     */
    private void setupListeners() {
        btnEntry.setOnClickListener(v -> registerEntry());
        btnExit.setOnClickListener(v -> registerExit());
    }

    /**
     * Regista a entrada de um veículo.
     */
    private void registerEntry() {
        String plate = etPlate.getText().toString().trim().toUpperCase();

        if (plate.isEmpty()) {
            Toast.makeText(getContext(), R.string.error_empty_plate, Toast.LENGTH_SHORT).show();
            return;
        }

        AppExecutors.io().execute(() -> {
            // Verificar se já está dentro
            ParkingEntity existing = parkingDao.getInsideByPlate(plate);

            if (existing != null) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> Toast
                            .makeText(getContext(), R.string.error_plate_already_inside, Toast.LENGTH_SHORT).show());
                }
                return;
            }

            // Criar novo registo
            ParkingEntity newEntry = new ParkingEntity(plate, System.currentTimeMillis());
            parkingDao.insert(newEntry);

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), R.string.success_entry, Toast.LENGTH_SHORT).show();
                    etPlate.setText("");
                    loadParkingRecords();
                });
            }
        });
    }

    /**
     * Regista a saída de um veículo.
     */
    private void registerExit() {
        String plate = etPlate.getText().toString().trim().toUpperCase();

        if (plate.isEmpty()) {
            Toast.makeText(getContext(), R.string.error_empty_plate, Toast.LENGTH_SHORT).show();
            return;
        }

        AppExecutors.io().execute(() -> {
            // Procurar veículo dentro
            ParkingEntity existing = parkingDao.getInsideByPlate(plate);

            if (existing == null) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> Toast
                            .makeText(getContext(), R.string.error_plate_not_inside, Toast.LENGTH_SHORT).show());
                }
                return;
            }

            // Atualizar registo com saída
            existing.exitTime = System.currentTimeMillis();
            existing.isInside = false;
            parkingDao.update(existing);

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), R.string.success_exit, Toast.LENGTH_SHORT).show();
                    etPlate.setText("");
                    loadParkingRecords();
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadParkingRecords();
    }

    /**
     * Carrega os registos de estacionamento da base de dados.
     */
    private void loadParkingRecords() {
        AppExecutors.io().execute(() -> {
            List<ParkingEntity> records = parkingDao.getAll();

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> updateUI(records));
            }
        });
    }

    /**
     * Atualiza a UI com os registos carregados.
     *
     * @param records Lista de registos de estacionamento
     */
    private void updateUI(@NonNull List<ParkingEntity> records) {
        adapter.setItems(records);

        // Mostrar estado vazio se não houver registos
        boolean isEmpty = records.isEmpty();
        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        emptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }
}
