package com.example.parquedeestacionamento.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parquedeestacionamento.R;
import com.example.parquedeestacionamento.data.local.entities.ParkingEntity;
import com.example.parquedeestacionamento.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter para exibir lista de registos de estacionamento.
 * Utiliza DiffUtil para atualizações eficientes da lista.
 */
public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ParkingViewHolder> {

    private final List<ParkingEntity> items = new ArrayList<>();
    private final SimpleDateFormat dateFormat;

    public ParkingAdapter() {
        this.dateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.getDefault());
    }

    /**
     * Atualiza a lista de itens usando DiffUtil para animações suaves.
     *
     * @param newItems Nova lista de registos de estacionamento
     */
    public void setItems(@NonNull List<ParkingEntity> newItems) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ParkingDiffCallback(items, newItems));
        items.clear();
        items.addAll(newItems);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parking, parent, false);
        return new ParkingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * ViewHolder para um item de estacionamento.
     */
    class ParkingViewHolder extends RecyclerView.ViewHolder {

        private final View statusIndicator;
        private final ImageView ivCar;
        private final TextView tvPlate;
        private final TextView tvTimes;
        private final TextView tvStatus;

        ParkingViewHolder(@NonNull View itemView) {
            super(itemView);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
            ivCar = itemView.findViewById(R.id.ivCar);
            tvPlate = itemView.findViewById(R.id.tvPlate);
            tvTimes = itemView.findViewById(R.id.tvTimes);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }

        void bind(@NonNull ParkingEntity entity) {
            tvPlate.setText(entity.plate);

            String entryFormatted = dateFormat.format(new Date(entity.entryTime));
            String exitFormatted = (entity.exitTime == null)
                    ? itemView.getContext().getString(R.string.still_inside)
                    : dateFormat.format(new Date(entity.exitTime));

            String timesText = itemView.getContext().getString(
                    R.string.parking_times,
                    entryFormatted,
                    exitFormatted);
            tvTimes.setText(timesText);

            // Configurar status badge e indicador
            if (entity.isInside) {
                tvStatus.setText("DENTRO");
                tvStatus.setBackgroundResource(R.drawable.bg_status_badge);
                statusIndicator.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.status_inside));
                ivCar.setColorFilter(
                        ContextCompat.getColor(itemView.getContext(), R.color.accent));
            } else {
                tvStatus.setText("SAIU");
                tvStatus.setBackgroundResource(R.drawable.bg_status_badge_outside);
                statusIndicator.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.status_outside));
                ivCar.setColorFilter(
                        ContextCompat.getColor(itemView.getContext(), R.color.text_secondary));
            }
        }
    }

    /**
     * DiffUtil Callback para calcular diferenças entre listas.
     */
    private static class ParkingDiffCallback extends DiffUtil.Callback {

        private final List<ParkingEntity> oldList;
        private final List<ParkingEntity> newList;

        ParkingDiffCallback(List<ParkingEntity> oldList, List<ParkingEntity> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).id == newList.get(newItemPosition).id;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }
}
