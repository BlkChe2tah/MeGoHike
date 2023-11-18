package com.example.megohike.common;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.megohike.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AppBottomSheetDialog {

    public static void createEquipmentBottomSheet(@NonNull Context context, @NonNull String name, @NonNull String count,@NonNull OnButtonClickListener listener) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.equipment_edit_layout);
        TextView tvName = bottomSheetDialog.findViewById(R.id.equipmentName);
        TextView tvCount = bottomSheetDialog.findViewById(R.id.equipmentCount);
        Button editBtn = bottomSheetDialog.findViewById(R.id.equipmentEditBtn);
        Button deleteBtn = bottomSheetDialog.findViewById(R.id.equipmentDeleteBtn);
        if (tvName != null) {
            tvName.setText(name);
        }
        if (tvCount != null) {
            tvCount.setText(count);
        }
        if (editBtn != null) {
            editBtn.setOnClickListener(v -> {
                if (bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                }
                listener.onEditButtonClick(v);
            });
        }
        if (deleteBtn != null) {
            deleteBtn.setOnClickListener(v -> {
                if (bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                }
                listener.onDeleteButtonClick(v);
            });
        }
        bottomSheetDialog.show();
    }

    public static void createObservationBottomSheet(@NonNull Context context, @NonNull String name, @NonNull String time, @NonNull String comment,@NonNull OnButtonClickListener listener) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.observation_edit_layout);
        TextView tvName = bottomSheetDialog.findViewById(R.id.observationName);
        TextView tvTime = bottomSheetDialog.findViewById(R.id.observationTime);
        TextView tvComment = bottomSheetDialog.findViewById(R.id.observationComment);
        Button editBtn = bottomSheetDialog.findViewById(R.id.observationEditBtn);
        Button deleteBtn = bottomSheetDialog.findViewById(R.id.observationDeleteBtn);
        if (tvName != null) {
            tvName.setText(name);
        }
        if (tvTime != null) {
            tvTime.setText(time);
        }
        if (tvComment != null) {
            tvComment.setText(comment);
            tvComment.setVisibility(comment.isEmpty() ? View.GONE : View.VISIBLE);
        }
        if (editBtn != null) {
            editBtn.setOnClickListener(v -> {
                if (bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                }
                listener.onEditButtonClick(v);
            });
        }
        if (deleteBtn != null) {
            deleteBtn.setOnClickListener(v -> {
                if (bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                }
                listener.onDeleteButtonClick(v);
            });
        }
        bottomSheetDialog.show();
    }

    public interface OnButtonClickListener {
        void onEditButtonClick(View view);
        void onDeleteButtonClick(View view);
    }


}
