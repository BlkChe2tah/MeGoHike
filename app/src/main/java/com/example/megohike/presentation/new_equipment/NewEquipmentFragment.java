package com.example.megohike.presentation.new_equipment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.megohike.R;
import com.example.megohike.common.AppDatePicker;
import com.example.megohike.common.AppTimePicker;
import com.example.megohike.common.InputTextWatcher;
import com.example.megohike.common.InputTimeConverter;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.use_case.AddNewEquipmentUseCaseImpl;
import com.example.megohike.data.use_case.AddNewObservationUseCaseImpl;
import com.example.megohike.databinding.FragmentNewEquipmentBinding;
import com.example.megohike.databinding.FragmentNewObservationBinding;
import com.example.megohike.presentation.new_equipment.view_model.NewEquipmentViewModel;
import com.example.megohike.presentation.new_equipment.view_model.NewEquipmentViewModelFactory;
import com.example.megohike.presentation.new_observation.NewObservationFragmentArgs;
import com.example.megohike.presentation.new_observation.view_model.NewObservationViewModel;
import com.example.megohike.presentation.new_observation.view_model.NewObservationViewModelFactory;
import com.google.android.material.textfield.TextInputLayout;

public class NewEquipmentFragment extends Fragment {

    private FragmentNewEquipmentBinding binding;
    private NewEquipmentViewModel viewModel;
    private String hikeInfoId = "0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final HikeInformationDatabase database = HikeInformationDatabase.getDatabase(getContext());
        viewModel = new ViewModelProvider(
                this, new NewEquipmentViewModelFactory(new AddNewEquipmentUseCaseImpl(database))
        ).get(NewEquipmentViewModel.class);
        hikeInfoId = NewEquipmentFragmentArgs.fromBundle(getArguments()).getInfoId();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewEquipmentBinding.inflate(inflater, container, false);
        setupTextListener();
        binding.saveBtn.setOnClickListener(v -> {
            final int id = Integer.parseInt(hikeInfoId);
            if (id != 0) {
                viewModel.save(id);
            } else {
                Toast.makeText(getContext(), "Unexpected error occurred", Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkEditTextErrorState(this.getViewLifecycleOwner(), viewModel.isNameEmpty, binding.nameTextLayout, getResources().getString(R.string.provide_equipment));
        checkEditTextErrorState(this.getViewLifecycleOwner(), viewModel.isCountCorrect, binding.countTextLayout, getResources().getString(R.string.provide_count));
        viewModel.getBtnEnableState.observe(this.getViewLifecycleOwner(), isEnable -> {
            binding.saveBtn.setEnabled(isEnable);
        });
        viewModel.getUiState.observe(this.getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess != null) {
                if (isSuccess) {
                    final NavController navController = Navigation.findNavController(view);
                    final NavBackStackEntry backStack = navController.getPreviousBackStackEntry();
                    if (backStack != null) {
                        backStack.getSavedStateHandle().set("back_result_equipment", true);
                    }
                    navController.popBackStack();
                    Toast.makeText(getContext(), "Data was successfully saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Unexpected error occurred while saving data", Toast.LENGTH_SHORT).show();
                }
            }
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
        binding.countEditText.addTextChangedListener(new InputTextWatcher(s -> viewModel.setCount(s)));
    }

}

