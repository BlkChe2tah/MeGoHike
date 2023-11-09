package com.example.megohike.presentation.hiking_detail;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.megohike.MainActivity;
import com.example.megohike.R;
import com.example.megohike.common.InputDateConverter;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.Equipment;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.data.data_source.database.entities.Observation;
import com.example.megohike.data.use_case.DeleteHikeInfoUseCaseImpl;
import com.example.megohike.data.use_case.LoadAllEquipmentsUseCaseImpl;
import com.example.megohike.data.use_case.LoadAllHikeInfoUseCaseImpl;
import com.example.megohike.data.use_case.LoadAllObservationsUseCaseImpl;
import com.example.megohike.databinding.FragmentHikingDetailBinding;
import com.example.megohike.domain.HikingLevel;
import com.example.megohike.presentation.hiking_detail.view_model.HikingDetailViewModel;
import com.example.megohike.presentation.hiking_detail.view_model.HikingDetailViewModelFactory;
import com.example.megohike.presentation.hiking_list.view_model.HikingListViewModel;
import com.example.megohike.presentation.hiking_list.view_model.HikingListViewModelFactory;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HikingDetailFragment extends Fragment implements ObservationInfoViewHolder.OnObservationItemClickListener {
    private FragmentHikingDetailBinding binding;
    private HikeInfo hikeInfo;

    private ObservationInfoAdapter adapter;

    private HikingDetailViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final MaterialToolbar toolbar = ((MainActivity)getContext()).findViewById(R.id.toolbar);
        toolbar.setTitle("");
        final HikeInformationDatabase database = HikeInformationDatabase.getDatabase(getContext());
        viewModel = new ViewModelProvider(
                this, new HikingDetailViewModelFactory(
                        new LoadAllObservationsUseCaseImpl(database),
                        new LoadAllEquipmentsUseCaseImpl(database),
                        new DeleteHikeInfoUseCaseImpl(database)
                )
        ).get(HikingDetailViewModel.class);
        adapter = new ObservationInfoAdapter(new ObservationInfoAdapter.ObservationDiff(), this);
        final Gson gson = new Gson();
        String string = HikingDetailFragmentArgs.fromBundle(getArguments()).getHikeInfo();
        hikeInfo = gson.fromJson(string, HikeInfo.class);
        viewModel.loadDetail(hikeInfo.getHikeInfoId());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentHikingDetailBinding.inflate(inflater, container, false);
        binding.hikingInfoObservation.setAdapter(adapter);
        setUpDetailView();
        binding.newItemBtn.setOnClickListener(v -> {
            final int id = hikeInfo.getHikeInfoId();
            Navigation.findNavController(v).navigate(
                    HikingDetailFragmentDirections.actionHikingDetailFragmentToNewEquipmentFragment(Integer.toString(id))
            );
        });
        binding.addNewEquipmentBtn.setOnClickListener(v -> {
            final int id = hikeInfo.getHikeInfoId();
            Navigation.findNavController(v).navigate(
                    HikingDetailFragmentDirections.actionHikingDetailFragmentToNewEquipmentFragment(Integer.toString(id))
            );
        });
        binding.deleteBtn.setOnClickListener(v -> {
            viewModel.delete(hikeInfo.getHikeInfoId());
        });
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getUiState.observe(this.getViewLifecycleOwner(), state -> {
            if (state == null) return;
            binding.contentLayout.setVisibility(state ? View.VISIBLE : View.GONE);
            binding.progressBarLayout.setVisibility(!state ? View.VISIBLE : View.GONE);
        });
        viewModel.getUiStateDelete.observe(this.getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess != null) {
                if (isSuccess) {
                    final NavController navController = Navigation.findNavController(view);
                    final NavBackStackEntry backStack = navController.getPreviousBackStackEntry();
                    if (backStack != null) {
                        backStack.getSavedStateHandle().set("back_result", true);
                    }
                    navController.popBackStack();
                    Toast.makeText(getContext(), "Data was successfully deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Unexpected error occurred while deleting data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getEquipments.observe(this.getViewLifecycleOwner(), items -> {
            binding.addNewEquipmentBtn.setVisibility(items == null ? View.VISIBLE : View.GONE);
            binding.equipmentFrame.setVisibility(items != null ? View.VISIBLE : View.GONE);
            if (items != null) {
                binding.equipmentChip.removeAllViews();
                for (int i = 0; i < items.size(); i++) {
                    final Equipment temp = items.get(i);
                    final String name = temp.getName();
                    final String count = Integer.toString(temp.getCount());
                    final String formatString = String.format("%sx %s", count, name);
                    final SpannableString spannableString = new SpannableString(formatString);
                    spannableString.setSpan(
                            new ForegroundColorSpan(getResources().getColor(R.color.md_theme_light_secondary)),
                            0, count.length() + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                    );
                    spannableString.setSpan(
                            new StyleSpan(Typeface.BOLD),
                            0, count.length() + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                    );
                    final Chip chip = new Chip(this.getContext());
                    chip.setText(spannableString);
                    chip.setEnsureMinTouchTargetSize(false);
                    binding.equipmentChip.addView(chip);
                }
            }
        });
        viewModel.getObservations.observe(this.getViewLifecycleOwner(), items -> {
            adapter.submitList(items);
        });
        final NavBackStackEntry backStack = Navigation.findNavController(view).getCurrentBackStackEntry();
        if (backStack != null) {
            backStack.getSavedStateHandle().<Boolean>getLiveData("back_result").observe(this.getViewLifecycleOwner(), value -> {
                if (value != null && value) {
                    backStack.getSavedStateHandle().set("back_result", null);
                    HikeInformationDatabase.databaseWriteExecutor.execute(() -> {
                        viewModel.loadObservations(hikeInfo.getHikeInfoId());
                    });
                }
            });
            backStack.getSavedStateHandle().<Boolean>getLiveData("back_result_equipment").observe(this.getViewLifecycleOwner(), value -> {
                if (value != null && value) {
                    backStack.getSavedStateHandle().set("back_result_equipment", null);
                    HikeInformationDatabase.databaseWriteExecutor.execute(() -> {
                        viewModel.loadEquipments(hikeInfo.getHikeInfoId());
                    });
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setUpDetailView() {
        final String parkingAvailableResult = hikeInfo.getParkingAvailable() == 0 ? "No" : "Yes";
        final String lengthFormat = String.format("%.1fkm", hikeInfo.getLengthOfHike());
        final HikingLevel hikingLevel = HikingLevel.values()[hikeInfo.getLevelOfDifficulty()];
        binding.hikingInfoName.setText(hikeInfo.getNameOfHike());
        binding.hikingInfoLength.setText(lengthFormat);
        binding.hikingInfoLevel.setText(hikingLevel.getLevel());
        binding.hikingInfoParkingAvailable.setText(parkingAvailableResult);
        // detail
        binding.hikingInfoLatitude.setText(hikeInfo.getLatitude());
        binding.hikingInfoLongitude.setText(hikeInfo.getLongitude());
        binding.hikingInfoStartDate.setText(InputDateConverter.convertTimeToDateString(hikeInfo.getStartDate()));
        binding.hikingInfoEndDate.setText(InputDateConverter.convertTimeToDateString(hikeInfo.getExpectedDate()));
    }

    @Override
    public void onClick(View view, Observation data) {
    }

    @Override
    public void onAddNewItemClick(View view) {
        final int id = hikeInfo.getHikeInfoId();
        Navigation.findNavController(view).navigate(
                HikingDetailFragmentDirections.actionHikingDetailFragmentToNewObservationFragment(Integer.toString(id))
        );
    }
}