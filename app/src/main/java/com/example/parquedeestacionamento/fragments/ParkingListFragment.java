package com.example.parquedeestacionamento.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

// Histórico: mostra a lista de todos os registos de estacionamento
public class ParkingListFragment extends Fragment {

    private ParkingAdapter adapter;
    private ParkingDao parkingDao;
    private RecyclerView recyclerView;
    private TextView emptyState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_parking_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parkingDao = AppDatabase.getInstance(requireContext()).parkingDao();

        recyclerView = view.findViewById(R.id.recyclerParking);
        emptyState = view.findViewById(R.id.emptyState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ParkingAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecords();
    }

    // Carrega os registos da base de dados e atualiza a lista
    private void loadRecords() {
        AppExecutors.io().execute(() -> {
            List<ParkingEntity> records = parkingDao.getAll();
            AppExecutors.main(() -> {
                adapter.setItems(records);
                boolean isEmpty = records.isEmpty();
                recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
                emptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            });
        });
    }
}
