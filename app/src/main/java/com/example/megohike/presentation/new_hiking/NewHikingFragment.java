package com.example.megohike.presentation.new_hiking;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.megohike.R;
import com.example.megohike.common.AppDatePicker;
import com.example.megohike.common.InputTextWatcher;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.use_case.AddNewHikeInfoUseCaseImpl;
import com.example.megohike.databinding.FragmentNewHikingBinding;
import com.example.megohike.domain.HikingLevel;
import com.example.megohike.presentation.new_hiking.view_model.NewHikingViewModel;
import com.example.megohike.presentation.new_hiking.view_model.NewHikingViewModelFactory;
import com.google.android.material.textfield.TextInputLayout;

public class NewHikingFragment extends Fragment implements AppDatePicker.DateSetListener {

    private static final String START_DATE_PICKER_FRAGMENT = "START_DATE_PICKER";
    private static final String EXPECTED_DATE_PICKER_FRAGMENT = "EXPECTED_DATE_PICKER";

    private FragmentNewHikingBinding binding;

    private NewHikingViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final HikeInformationDatabase database = HikeInformationDatabase.getDatabase(getContext());
        viewModel = new ViewModelProvider(
                this, new NewHikingViewModelFactory(new AddNewHikeInfoUseCaseImpl(database))
        ).get(NewHikingViewModel.class);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentNewHikingBinding.inflate(inflater, container, false);
        setupTextListener();
        setupLevelDropDown(getContext());
        setupParkingAvailableListener();
        binding.startDateTextLayout.setEndIconOnClickListener(v -> {
            if (getActivity() != null) {
                final AppDatePicker.DatePickerFragment datePickerFragment = new AppDatePicker.DatePickerFragment(START_DATE_PICKER_FRAGMENT,this);
                datePickerFragment.show(getActivity().getSupportFragmentManager(), START_DATE_PICKER_FRAGMENT);
            }
        });
        binding.expectedDateTextLayout.setEndIconOnClickListener(v -> {
            if (getActivity() != null) {
                final AppDatePicker.DatePickerFragment datePickerFragment = new AppDatePicker.DatePickerFragment(EXPECTED_DATE_PICKER_FRAGMENT,this);
                datePickerFragment.show(getActivity().getSupportFragmentManager(), EXPECTED_DATE_PICKER_FRAGMENT);
            }
        });
        binding.saveBtn.setOnClickListener(v -> viewModel.save());
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkEditTextErrorState(this.getViewLifecycleOwner(), viewModel.isNameEmpty, binding.nameTextLayout, getResources().getString(R.string.provide_name_of_hike));
        checkEditTextErrorState(this.getViewLifecycleOwner(), viewModel.isLengthEmpty, binding.lengthTextLayout, getResources().getString(R.string.provide_length_of_hike));
        checkEditTextErrorState(this.getViewLifecycleOwner(), viewModel.isStartDateFormatCorrect, binding.startDateTextLayout, getResources().getString(R.string.enter_valid_date_format));
        checkEditTextErrorState(this.getViewLifecycleOwner(), viewModel.isExpectedDateFormatCorrect, binding.expectedDateTextLayout, getResources().getString(R.string.enter_valid_date_format));
        viewModel.getBtnEnableState.observe(this.getViewLifecycleOwner(), isEnable -> {
            binding.saveBtn.setEnabled(isEnable);
        });
        viewModel.getUiState.observe(this.getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess != null) {
                if (isSuccess) {
                    final NavController navController = Navigation.findNavController(view);
                    final NavBackStackEntry backStack = navController.getPreviousBackStackEntry();
                    if (backStack != null) {
                        backStack.getSavedStateHandle().set("back_result", true);
                    }
                    navController.popBackStack();
                    Toast.makeText(getContext(), "Data was successfully saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Unexpected error occurred while saving data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
        binding.latitudeEditText.addTextChangedListener(new InputTextWatcher(s -> viewModel.setLatitude(s)));
        binding.longitudeEditText.addTextChangedListener(new InputTextWatcher(s -> viewModel.setLongitude(s)));
        binding.lengthEditText.addTextChangedListener(new InputTextWatcher(s -> viewModel.setLength(s)));
        binding.startDateEditText.addTextChangedListener(new InputTextWatcher(s -> viewModel.setStartDate(s)));
        binding.expectedDateEditText.addTextChangedListener(new InputTextWatcher(s -> viewModel.setExpectedDate(s)));
    }

    private void setupLevelDropDown(Context context) {
        final HikingLevel[] levels = HikingLevel.values();
        final String[] items = new String[levels.length];
        for (int i = 0; i < levels.length; i++) {
            items[i] = levels[i].getLevel();
        }
        binding.levelDropDown.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, items));
        binding.levelDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setHikingLevel(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupParkingAvailableListener() {
        binding.parkingAvailableSwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> viewModel.setParkingAvailable(isChecked)
        );
    }

    @Override
    public void onDateSet(String date, String tag) {
        if (tag.equals(START_DATE_PICKER_FRAGMENT)) {
            setEditText(binding.startDateEditText, date);
        }
        if (tag.equals(EXPECTED_DATE_PICKER_FRAGMENT)) {
            setEditText(binding.expectedDateEditText, date);
        }
    }

    private void setEditText(EditText view, String text) {
        view.setText(text, TextView.BufferType.EDITABLE);
        view.setSelection(text.length());
    }
}