package com.example.megohike.presentation.hiking_detail;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.megohike.data.data_source.database.entities.Observation;

public class ObservationInfoAdapter extends ListAdapter<Observation, ObservationInfoViewHolder> {

    private final ObservationInfoViewHolder.OnObservationItemClickListener listener;
    public ObservationInfoAdapter(@NonNull DiffUtil.ItemCallback<Observation> diffCallback, @NonNull ObservationInfoViewHolder.OnObservationItemClickListener listener) {
        super(diffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ObservationInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ObservationInfoViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ObservationInfoViewHolder holder, int position) {
        Observation current = getItem(position);
        holder.bind(current, listener);
    }

    static class ObservationDiff extends DiffUtil.ItemCallback<Observation> {

        @Override
        public boolean areItemsTheSame(@NonNull Observation oldItem, @NonNull Observation newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Observation oldItem, @NonNull Observation newItem) {
            return oldItem.getObservationId() == newItem.getObservationId();
        }
    }
}
