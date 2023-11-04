package com.example.megohike.presentation.hiking_list;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.megohike.data.data_source.database.entities.HikeInfo;

public class HikingInfoAdapter extends ListAdapter<HikeInfo, HikingInfoViewHolder> {

    private final HikingInfoViewHolder.OnHikingItemClickListener listener;
    public HikingInfoAdapter(@NonNull DiffUtil.ItemCallback<HikeInfo> diffCallback, @NonNull HikingInfoViewHolder.OnHikingItemClickListener listener) {
        super(diffCallback);
        this.listener = listener;
    }

    @Override
    public HikingInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return HikingInfoViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(HikingInfoViewHolder holder, int position) {
        HikeInfo current = getItem(position);
        holder.bind(current, listener);
    }

    static class HikeInfoDiff extends DiffUtil.ItemCallback<HikeInfo> {

        @Override
        public boolean areItemsTheSame(@NonNull HikeInfo oldItem, @NonNull HikeInfo newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull HikeInfo oldItem, @NonNull HikeInfo newItem) {
            return oldItem.getHikeInfoId() == newItem.getHikeInfoId();
        }
    }
}
