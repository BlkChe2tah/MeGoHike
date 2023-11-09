package com.example.megohike.presentation.hiking_detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.megohike.R;
import com.example.megohike.common.InputDateConverter;
import com.example.megohike.data.data_source.database.entities.Observation;

class ObservationInfoViewHolder extends RecyclerView.ViewHolder {
    private final View itemView;
    private final View addNewContainer;
    private final View observationContainer;
    private final TextView name;
    private final TextView time;
    private final TextView comment;

    public interface OnObservationItemClickListener {
        void onClick(View view, Observation data);
        void onAddNewItemClick(View view);
    }

    private ObservationInfoViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        addNewContainer = itemView.findViewById(R.id.observationNewContainer);
        observationContainer = itemView.findViewById(R.id.observationContainer);
        name = itemView.findViewById(R.id.observationName);
        time = itemView.findViewById(R.id.observationTime);
        comment = itemView.findViewById(R.id.observationComment);
    }

    public void bind(@Nullable Observation data, @NonNull OnObservationItemClickListener listener) {
        addNewContainer.setVisibility(data == null ? View.VISIBLE : View.GONE);
        observationContainer.setVisibility(data != null ? View.VISIBLE : View.GONE);
        if (data != null) {
            time.setText(InputDateConverter.convertTimeToDateTimeString(data.getTime()));
            name.setText(data.getObservation());
            comment.setText(data.getComment());
        }
        itemView.setOnClickListener(v -> {
            if (data == null) {
                listener.onAddNewItemClick(v);
            } else {
                listener.onClick(v, data);
            }
        });
    }

    static ObservationInfoViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hiking_info_observation_item, parent, false);
        return new ObservationInfoViewHolder(view);
    }
}

