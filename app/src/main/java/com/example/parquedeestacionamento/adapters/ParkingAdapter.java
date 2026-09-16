package com.example.parquedeestacionamento.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parquedeestacionamento.R;
import com.example.parquedeestacionamento.data.local.entities.ParkingEntity;
import com.example.parquedeestacionamento.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Adapter para a RecyclerView do histórico de estacionamento
public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ViewHolder> {

    private final List<ParkingEntity> items = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.getDefault());

    // Atualiza a lista de registos
    public void setItems(@NonNull List<ParkingEntity> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder: liga os dados de um registo às views
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlate, tvTimes, tvStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlate = itemView.findViewById(R.id.tvPlate);
            tvTimes = itemView.findViewById(R.id.tvTimes);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }

        void bind(@NonNull ParkingEntity e) {
            tvPlate.setText(e.plate);

            String entry = dateFormat.format(new Date(e.entryTime));
            String exit = (e.exitTime == null)
                    ? itemView.getContext().getString(R.string.still_inside)
                    : dateFormat.format(new Date(e.exitTime));
            tvTimes.setText(itemView.getContext().getString(R.string.parking_times, entry, exit));

            tvStatus.setText(e.isInside ? "DENTRO" : "SAIU");
        }
    }
}
