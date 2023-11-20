package com.example.megohike.presentation.advance_search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.megohike.R;
import com.example.megohike.common.AppDatePicker;
import com.example.megohike.common.InputTextWatcher;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.use_case.LoadAllHikeInfoUseCaseImpl;
import com.example.megohike.databinding.FragmentAdvanceSearchBinding;
import com.example.megohike.domain.HikingLevel;
import com.example.megohike.presentation.main_activity.view_model.HikingListViewModel;
import com.example.megohike.presentation.main_activity.view_model.HikingListViewModelFactory;

import java.util.Objects;

public class AdvanceSearchFragment extends Fragment implements AppDatePicker.DateSetListener {
    private static final String START_DATE_PICKER_FRAGMENT = "START_DATE_PICKER";

    private FragmentAdvanceSearchBinding binding;
    private HikingListViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final HikeInformationDatabase database = HikeInformationDatabase.getDatabase(getContext());
        viewModel = new ViewModelProvider(requireActivity(), new HikingListViewModelFactory(new LoadAllHikeInfoUseCaseImpl(database))).get(HikingListViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdvanceSearchBinding.inflate(inflater, container, false);
        setupView();
        binding.startDateTextLayout.setEndIconOnClickListener(v -> {
            if (getActivity() != null) {
                final AppDatePicker.DatePickerFragment datePickerFragment = new AppDatePicker.DatePickerFragment(START_DATE_PICKER_FRAGMENT,this);
                datePickerFragment.show(getActivity().getSupportFragmentManager(), START_DATE_PICKER_FRAGMENT);
            }
        });
        binding.clearBtn.setOnClickListener(v -> {
            clearData();
        });
        binding.applyBtn.setOnClickListener(v -> {
            if (viewModel.isDateFormatCorrect()) {
                final NavController navController = Navigation.findNavController(binding.getRoot());
                navController.popBackStack();
            } else  {
                Toast.makeText(requireContext(), getResources().getString(R.string.enter_valid_date_format), Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupTextListener();
    }

    private void setupTextListener() {
        binding.nameEditText.addTextChangedListener(new InputTextWatcher(s -> viewModel.setHikeName(s)));
        binding.startDateEditText.addTextChangedListener(new InputTextWatcher(s -> viewModel.setStartDate(s)));
    }

    private void setupView() {
        if (viewModel.getHikeName() != null) {
            binding.nameEditText.setText(viewModel.getHikeName(), TextView.BufferType.EDITABLE);
        }
        if (viewModel.getStartDate() != null) {
            binding.startDateEditText.setText(viewModel.getStartDate(), TextView.BufferType.EDITABLE);
        }
        setupLevelDropDown();
    }

    private void setupLevelDropDown() {
        final HikingLevel[] levels = HikingLevel.values();
        final String[] items = new String[levels.length + 1];
        items[0] = "All";
        for (int i = 0; i < levels.length; i++) {
            items[i + 1] = levels[i].getLevel();
        }
        binding.levelDropDown.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, items));
        binding.levelDropDown.setSelection(viewModel.getLevel() + 1);
        binding.levelDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setLevel(position - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onDateSet(String date, String tag) {
        binding.startDateEditText.setText(date, TextView.BufferType.EDITABLE);
        binding.startDateEditText.setSelection(date.length());
    }

    private void clearData() {
        binding.nameEditText.setText("", TextView.BufferType.EDITABLE);
        binding.startDateEditText.setText("", TextView.BufferType.EDITABLE);
        binding.nameEditText.clearFocus();
        binding.startDateEditText.clearFocus();
        binding.levelDropDown.setSelection(0);
    }

}

