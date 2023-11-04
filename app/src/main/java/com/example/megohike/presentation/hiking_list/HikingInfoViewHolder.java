package com.example.megohike.presentation.hiking_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.megohike.R;
import com.example.megohike.common.InputDateConverter;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.domain.HikingLevel;

class HikingInfoViewHolder extends RecyclerView.ViewHolder {
    private final View itemView;
    private final TextView name;
    private final TextView date;
    private final TextView level;

    public interface OnHikingItemClickListener {
        void onClick(View view, HikeInfo data);
    }

    private HikingInfoViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        name = itemView.findViewById(R.id.hikingInfoItemText);
        date = itemView.findViewById(R.id.hikingInfoItemDate);
        level = itemView.findViewById(R.id.hikingInfoItemLevel);
    }

    public void bind(HikeInfo data,@NonNull OnHikingItemClickListener listener) {
        name.setText(data.getNameOfHike());
        date.setText(InputDateConverter.convertTimeToDateString(data.getStartDate()));
        final HikingLevel hikingLevel = HikingLevel.values()[data.getLevelOfDifficulty()];
        final String levelStr = String.format("Level of Difficulty - %s", hikingLevel.getLevel());
        level.setText(levelStr);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, data);
            }
        });
    }

    static HikingInfoViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hiking_info_item, parent, false);
        return new HikingInfoViewHolder(view);
    }
}

