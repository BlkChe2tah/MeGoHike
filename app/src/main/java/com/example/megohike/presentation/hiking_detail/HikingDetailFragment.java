package com.example.megohike.presentation.hiking_detail;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.megohike.R;
import com.example.megohike.common.AppBottomSheetDialog;
import com.example.megohike.common.InputDateConverter;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.Equipment;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.data.data_source.database.entities.Observation;
import com.example.megohike.data.use_case.DeleteEquipmentUseCaseImpl;
import com.example.megohike.data.use_case.DeleteHikeInfoUseCaseImpl;
import com.example.megohike.data.use_case.DeleteObservationUseCaseImpl;
import com.example.megohike.data.use_case.HikeInfoDetailUseCaseImpl;
import com.example.megohike.data.use_case.LoadAllEquipmentsUseCaseImpl;
import com.example.megohike.data.use_case.LoadAllHikeInfoUseCaseImpl;
import com.example.megohike.data.use_case.LoadAllObservationsUseCaseImpl;
import com.example.megohike.databinding.FragmentHikingDetailBinding;
import com.example.megohike.domain.HikingLevel;
import com.example.megohike.presentation.hiking_detail.view_model.HikingDetailViewModel;
import com.example.megohike.presentation.hiking_detail.view_model.HikingDetailViewModelFactory;
import com.example.megohike.presentation.main_activity.view_model.HikingListViewModel;
import com.example.megohike.presentation.main_activity.view_model.HikingListViewModelFactory;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

public class HikingDetailFragment extends Fragment implements ObservationInfoViewHolder.OnObservationItemClickListener {
    private FragmentHikingDetailBinding binding;
    private int hikeInfoId = 0;

    private ObservationInfoAdapter adapter;

    private HikingDetailViewModel viewModel;
    private HikingListViewModel listViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final HikeInformationDatabase database = HikeInformationDatabase.getDatabase(getContext());
        listViewModel = new ViewModelProvider(requireActivity(), new HikingListViewModelFactory(new LoadAllHikeInfoUseCaseImpl(database))).get(HikingListViewModel.class);
        viewModel = new ViewModelProvider(
                this, new HikingDetailViewModelFactory(
                        new LoadAllObservationsUseCaseImpl(database),
                        new LoadAllEquipmentsUseCaseImpl(database),
                        new DeleteHikeInfoUseCaseImpl(database),
                        new DeleteEquipmentUseCaseImpl(database),
                        new DeleteObservationUseCaseImpl(database),
                        new HikeInfoDetailUseCaseImpl(database)
                )
        ).get(HikingDetailViewModel.class);
        adapter = new ObservationInfoAdapter(new ObservationInfoAdapter.ObservationDiff(), this);
        final String idString = HikingDetailFragmentArgs.fromBundle(getArguments()).getHikeInfoId();
        if (idString != null && !idString.isEmpty()) {
            hikeInfoId = Integer.parseInt(idString);
            viewModel.loadDetail(hikeInfoId);
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentHikingDetailBinding.inflate(inflater, container, false);
        binding.hikingInfoObservation.setAdapter(adapter);
        binding.newItemBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(
                    HikingDetailFragmentDirections.actionHikingDetailFragmentToNewEquipmentFragment(null, Integer.toString(hikeInfoId))
            );
        });
        binding.addNewEquipmentBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(
                    HikingDetailFragmentDirections.actionHikingDetailFragmentToNewEquipmentFragment(null, Integer.toString(hikeInfoId))
            );
        });
        binding.editBtn.setOnClickListener(v -> {
            final Gson gson = new Gson();
            Navigation.findNavController(v).navigate(
                    HikingDetailFragmentDirections.actionHikingDetailFragmentToNewHikingFragment(gson.toJson(viewModel.hikeInfo.getValue()), "edit")
            );
        });
        binding.deleteBtn.setOnClickListener(v -> {
            viewModel.delete(hikeInfoId);
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
        viewModel.hikeInfo.observe(this.getViewLifecycleOwner(), data -> {
            if (data != null) {
                setUpDetailView(data);
            }
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
                    chip.setOnClickListener(v -> {
                        AppBottomSheetDialog.createEquipmentBottomSheet(requireContext(), name, count, new AppBottomSheetDialog.OnButtonClickListener() {
                            @Override
                            public void onEditButtonClick(View view) {
                                final Gson gson = new Gson();
                                Navigation.findNavController(v).navigate(
                                        HikingDetailFragmentDirections.actionHikingDetailFragmentToNewEquipmentFragment(gson.toJson(temp), null)
                                );
                            }

                            @Override
                            public void onDeleteButtonClick(View view) {
                                viewModel.deleteEquipment(hikeInfoId, temp.getEquipmentId());
                            }
                        });
                    });
                    binding.equipmentChip.addView(chip);
                }
            }
        });
        viewModel.getObservations.observe(this.getViewLifecycleOwner(), items -> {
            adapter.submitList(items);
        });
        final NavBackStackEntry backStack = Navigation.findNavController(view).getCurrentBackStackEntry();
        if (backStack != null) {
            backStack.getSavedStateHandle().<Boolean>getLiveData("back_result_confirm").observe(this.getViewLifecycleOwner(), value -> {
                if (value != null && value) {
                    backStack.getSavedStateHandle().set("back_result_confirm", null);
                    HikeInformationDatabase.databaseWriteExecutor.execute(() -> {
                        viewModel.loadHikeInfo(hikeInfoId);
                        listViewModel.loadHikingList();
                    });
                }
            });
            backStack.getSavedStateHandle().<Boolean>getLiveData("back_result").observe(this.getViewLifecycleOwner(), value -> {
                if (value != null && value) {
                    backStack.getSavedStateHandle().set("back_result", null);
                    HikeInformationDatabase.databaseWriteExecutor.execute(() -> {
                        viewModel.loadObservations(hikeInfoId);
                    });
                }
            });
            backStack.getSavedStateHandle().<Boolean>getLiveData("back_result_equipment").observe(this.getViewLifecycleOwner(), value -> {
                if (value != null && value) {
                    backStack.getSavedStateHandle().set("back_result_equipment", null);
                    HikeInformationDatabase.databaseWriteExecutor.execute(() -> {
                        viewModel.loadEquipments(hikeInfoId);
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

    private void setUpDetailView(HikeInfo hikeInfo) {
        final String lengthFormat = String.format("%.1fkm", hikeInfo.getLengthOfHike());
        final HikingLevel hikingLevel = HikingLevel.values()[hikeInfo.getLevelOfDifficulty()];
        binding.hikingInfoName.setText(hikeInfo.getNameOfHike());
        binding.hikingInfoLength.setText(lengthFormat);
        binding.hikingInfoLevel.setText(hikingLevel.getLevel());
        // detail
        binding.hikingInfoLatitude.setText(hikeInfo.getLatitude());
        binding.hikingInfoLongitude.setText(hikeInfo.getLongitude());
        binding.hikingInfoStartDate.setText(InputDateConverter.convertTimeToDateString(hikeInfo.getStartDate()));
        binding.hikingInfoEndDate.setText(InputDateConverter.convertTimeToDateString(hikeInfo.getExpectedDate()));
    }

    @Override
    public void onClick(View view, Observation data) {
        AppBottomSheetDialog.createObservationBottomSheet(requireContext(), data.getObservation(), InputDateConverter.convertTimeToDateTimeString(data.getTime()),
                data.getComment(), new AppBottomSheetDialog.OnButtonClickListener() {
            @Override
            public void onEditButtonClick(View view) {
                final Gson gson = new Gson();
                Navigation.findNavController(binding.getRoot()).navigate(
                        HikingDetailFragmentDirections.actionHikingDetailFragmentToNewObservationFragment(gson.toJson(data), null)
                );
            }

            @Override
            public void onDeleteButtonClick(View view) {
                viewModel.deleteObservation(hikeInfoId, data.getObservationId());
            }
        });
    }

    @Override
    public void onAddNewItemClick(View view) {
        Navigation.findNavController(view).navigate(
                HikingDetailFragmentDirections.actionHikingDetailFragmentToNewObservationFragment(null, Integer.toString(hikeInfoId))
        );
    }
}