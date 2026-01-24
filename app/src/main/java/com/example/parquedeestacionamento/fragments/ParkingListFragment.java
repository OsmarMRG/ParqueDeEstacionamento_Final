package com.example.parquedeestacionamento.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parquedeestacionamento.R;
import com.example.parquedeestacionamento.adapters.ParkingAdapter;
import com.example.parquedeestacionamento.data.local.AppDatabase;
import com.example.parquedeestacionamento.utils.AppExecutors;

public class ParkingListFragment extends Fragment {

    private ParkingAdapter adapter;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_parking_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = v.findViewById(R.id.recyclerParking);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ParkingAdapter();
        rv.setAdapter(adapter);

        db = AppDatabase.getInstance(requireContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        AppExecutors.io().execute(() -> {
            var data = db.parkingDao().getAll();
            requireActivity().runOnUiThread(() -> adapter.setItems(data));
        });
    }
}
