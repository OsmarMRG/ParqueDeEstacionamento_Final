package com.example.parquedeestacionamento.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parquedeestacionamento.R;
import com.example.parquedeestacionamento.data.local.entities.ParkingEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.VH> {

    private final List<ParkingEntity> items = new ArrayList<>();
    private final SimpleDateFormat df = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());

    public void setItems(List<ParkingEntity> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parking, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        ParkingEntity e = items.get(pos);

        h.tvPlate.setText(e.plate);

        String entry = df.format(new Date(e.entryTime));
        String exit = (e.exitTime == null) ? "Ainda dentro" : df.format(new Date(e.exitTime));
        h.tvTimes.setText("Entrada: " + entry + " | Saída: " + exit);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvPlate, tvTimes;
        VH(@NonNull View itemView) {
            super(itemView);
            tvPlate = itemView.findViewById(R.id.tvPlate);
            tvTimes = itemView.findViewById(R.id.tvTimes);
        }
    }
}
