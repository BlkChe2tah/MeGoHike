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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.megohike.R;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.use_case.AddNewHikeInfoUseCaseImpl;
import com.example.megohike.databinding.FragmentNewHikingBinding;
import com.example.megohike.domain.HikingLevel;
import com.example.megohike.presentation.new_hiking.view_model.NewHikingViewModel;
import com.example.megohike.presentation.new_hiking.view_model.NewHikingViewModelFactory;

public class NewHikingFragment extends Fragment {

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
        setupLevelDropDown(getContext());
        setupNameOfHikeTextListener();
        setupLengthOfHikeTextListener();
        setupStartDateTextListener();
        setupExpectedDateTextListener();
        setupLatitudeTextListener();
        setupLongitudeTextListener();
        setupParkingAvailableListener();
        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.save();
            }
        });
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getNameState().observe(this.getViewLifecycleOwner(), isError -> {
            if (isError != null && isError) {
                binding.nameTextLayout.setError(getResources().getString(R.string.provide_name_of_hike));
            } else {
                binding.nameTextLayout.setError(null);
            }
        });
        viewModel.getLengthState().observe(this.getViewLifecycleOwner(), isError -> {
            if (isError != null && isError) {
                binding.lengthTextLayout.setError(getResources().getString(R.string.provide_length_of_hike));
            } else {
                binding.lengthTextLayout.setError(null);
            }
        });
        viewModel.getStartDateState().observe(this.getViewLifecycleOwner(), isError -> {
            if (isError != null && isError) {
                binding.startDateTextLayout.setError(getResources().getString(R.string.enter_valid_date_format));
            } else {
                binding.startDateTextLayout.setError(null);
            }
        });
        viewModel.getExpectedDateState().observe(this.getViewLifecycleOwner(), isError -> {
            if (isError != null && isError) {
                binding.expectedDateTextLayout.setError(getResources().getString(R.string.enter_valid_date_format));
            } else {
                binding.expectedDateTextLayout.setError(null);
            }
        });
        viewModel.getIsBtnEnableState().observe(this.getViewLifecycleOwner(), isEnable -> {
            binding.saveBtn.setEnabled(isEnable);
        });
        viewModel.getSaveHikeInfoState().observe(this.getViewLifecycleOwner(), isSuccess -> {
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

    private void setupNameOfHikeTextListener() {
        binding.nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupLengthOfHikeTextListener() {
        binding.lengthEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setLength(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupStartDateTextListener() {
        binding.startDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setStartDate(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupExpectedDateTextListener() {
        binding.expectedDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setExpectedDate(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupLatitudeTextListener() {
        binding.latitudeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setLatitude(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupLongitudeTextListener() {
        binding.longitudeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setLongitude(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupParkingAvailableListener() {
        binding.parkingAvailableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                                      @Override
                                                                      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                          viewModel.setParkingAvailable(isChecked);
                                                                      }
                                                                  }
        );
    }

}