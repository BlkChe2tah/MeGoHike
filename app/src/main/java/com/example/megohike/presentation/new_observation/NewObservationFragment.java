package com.example.megohike.presentation.new_observation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.megohike.R;
import com.example.megohike.common.AppDatePicker;
import com.example.megohike.common.AppTimePicker;
import com.example.megohike.common.InputDateConverter;
import com.example.megohike.common.InputTextWatcher;
import com.example.megohike.common.InputTimeConverter;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.Equipment;
import com.example.megohike.data.data_source.database.entities.Observation;
import com.example.megohike.data.use_case.AddNewHikeInfoUseCaseImpl;
import com.example.megohike.data.use_case.AddNewObservationUseCaseImpl;
import com.example.megohike.databinding.FragmentNewObservationBinding;
import com.example.megohike.presentation.hiking_detail.HikingDetailFragmentArgs;
import com.example.megohike.presentation.new_equipment.NewEquipmentFragmentArgs;
import com.example.megohike.presentation.new_hiking.view_model.NewHikingViewModel;
import com.example.megohike.presentation.new_hiking.view_model.NewHikingViewModelFactory;
import com.example.megohike.presentation.new_observation.view_model.NewObservationViewModel;
import com.example.megohike.presentation.new_observation.view_model.NewObservationViewModelFactory;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

public class NewObservationFragment extends Fragment implements AppDatePicker.DateSetListener, AppTimePicker.TimeSetListener {

    private static final String DATE_PICKER_FRAGMENT = "DATE_PICKER";
    private static final String TIME_PICKER_FRAGMENT = "TIME_PICKER";

    private FragmentNewObservationBinding binding;
    private NewObservationViewModel viewModel;
    private String hikeInfoId = "0";
    private Observation observation = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final HikeInformationDatabase database = HikeInformationDatabase.getDatabase(getContext());
        viewModel = new ViewModelProvider(
                this, new NewObservationViewModelFactory()
        ).get(NewObservationViewModel.class);
        hikeInfoId = NewObservationFragmentArgs.fromBundle(getArguments()).getInfoId();
        final String observationData = NewObservationFragmentArgs.fromBundle(getArguments()).getObservationData();
        if (observationData != null) {
            final Gson gson = new Gson();
            observation = gson.fromJson(observationData, Observation.class);
            hikeInfoId = Integer.toString(observation.getHikeInfoId());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewObservationBinding.inflate(inflater, container, false);
        setupTextListener();
        binding.dateTextLayout.setEndIconOnClickListener(v -> {
            if (getActivity() != null) {
                final AppDatePicker.DatePickerFragment datePickerFragment = new AppDatePicker.DatePickerFragment(DATE_PICKER_FRAGMENT,this);
                datePickerFragment.show(getActivity().getSupportFragmentManager(), DATE_PICKER_FRAGMENT);
            }
        });
        binding.timeTextLayout.setEndIconOnClickListener(v -> {
            if (getActivity() != null) {
                final AppTimePicker.TimePickerFragment timePickerFragment = new AppTimePicker.TimePickerFragment(TIME_PICKER_FRAGMENT,this);
                timePickerFragment.show(getActivity().getSupportFragmentManager(), TIME_PICKER_FRAGMENT);
            }
        });
        binding.saveBtn.setOnClickListener(v -> {
            final int id = Integer.parseInt(hikeInfoId);
            if (id != 0) {
                final int observationId = observation == null ? 0 : observation.getObservationId();
                final Observation data = viewModel.loadObservation(id, observationId);
                Navigation.findNavController(binding.getRoot()).navigate(
                        NewObservationFragmentDirections.actionNewObservationFragmentToNewObservationConfirmFragment(new Gson().toJson(data))
                );
            } else {
                Toast.makeText(getContext(), "Unexpected error occurred", Toast.LENGTH_SHORT).show();
            }
        });
        setupView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkEditTextErrorState(this.getViewLifecycleOwner(), viewModel.isNameEmpty, binding.nameTextLayout, getResources().getString(R.string.provide_observation));
        checkEditTextErrorState(this.getViewLifecycleOwner(), viewModel.isDateFormatCorrect, binding.dateTextLayout, getResources().getString(R.string.enter_valid_date_format));
        checkEditTextErrorState(this.getViewLifecycleOwner(), viewModel.isTimeFormatCorrect, binding.timeTextLayout, getResources().getString(R.string.enter_valid_time_format));
        viewModel.getBtnEnableState.observe(this.getViewLifecycleOwner(), isEnable -> {
            binding.saveBtn.setEnabled(isEnable);
        });
    }

    private void checkEditTextErrorState(@NonNull LifecycleOwner lifecycleOwner, LiveData<Boolean> isError, TextInputLayout inputLayout, String errorMessage) {
        isError.observe(lifecycleOwner, error -> {
            if (error != null && error) {
                inputLayout.setError(errorMessage);
            } else  {
                inputLayout.setError(null);
            }
        });
    }

    private void setupTextListener() {
        binding.nameEditText.addTextChangedListener(new InputTextWatcher(s -> viewModel.setName(s)));
        binding.dateEditText.addTextChangedListener(new InputTextWatcher(s -> viewModel.setDate(s)));
        binding.timeEditText.addTextChangedListener(new InputTextWatcher(s -> viewModel.setTime(s)));
        binding.commentEditText.addTextChangedListener(new InputTextWatcher(s -> viewModel.setComment(s)));
    }

    @Override
    public void onDateSet(String date, String tag) {
        binding.dateEditText.setText(date, TextView.BufferType.EDITABLE);
        binding.dateEditText.setSelection(date.length());
    }

    @Override
    public void onTimeSet(String time, String tag) {
        final String result = InputTimeConverter.convert24to12(time);
        binding.timeEditText.setText(result, TextView.BufferType.EDITABLE);
        binding.timeEditText.setSelection(result.length());
    }

    private void setupView() {
        if (observation != null) {
            binding.nameEditText.setText(observation.getObservation(), TextView.BufferType.EDITABLE);
            binding.dateEditText.setText(InputDateConverter.getDate(observation.getTime()), TextView.BufferType.EDITABLE);
            binding.timeEditText.setText(InputDateConverter.getTime(observation.getTime()), TextView.BufferType.EDITABLE);
            binding.commentEditText.setText(observation.getComment(), TextView.BufferType.EDITABLE);
        }
    }

}

