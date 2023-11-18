package com.example.megohike.presentation.new_hiking_confirm;

import android.os.Bundle;
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
import com.example.megohike.common.InputDateConverter;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.data.use_case.AddNewHikeInfoUseCaseImpl;
import com.example.megohike.databinding.FragmentNewHikingConfirmBinding;
import com.example.megohike.domain.HikingLevel;
import com.example.megohike.presentation.new_hiking.NewHikingFragmentArgs;
import com.example.megohike.presentation.new_hiking_confirm.view_model.NewHikingConfirmViewModel;
import com.example.megohike.presentation.new_hiking_confirm.view_model.NewHikingConfirmViewModelFactory;
import com.google.gson.Gson;

import java.util.Objects;

public class NewHikingConfirmFragment extends Fragment {
    private FragmentNewHikingConfirmBinding binding;
    private NewHikingConfirmViewModel viewModel;

    private String type = "new";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final HikeInformationDatabase database = HikeInformationDatabase.getDatabase(getContext());
        viewModel = new ViewModelProvider(
                this, new NewHikingConfirmViewModelFactory(
                new AddNewHikeInfoUseCaseImpl(database)
                )
        ).get(NewHikingConfirmViewModel.class);
        final Gson gson = new Gson();
        String mType = NewHikingFragmentArgs.fromBundle(getArguments()).getType();
        String string = NewHikingConfirmFragmentArgs.fromBundle(getArguments()).getHikeInfo();
        type = mType == null ? "new" : mType;
        viewModel.setHikeInfoData(gson.fromJson(string, HikeInfo.class));
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentNewHikingConfirmBinding.inflate(inflater, container, false);
        binding.confirmBtn.setOnClickListener(v -> {
            viewModel.save();
        });
        binding.cancelBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpDetailView();
        viewModel.getUiState.observe(this.getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess != null) {
                if (isSuccess) {
                    final NavController navController = Navigation.findNavController(view);
                    final int backStackId = Objects.equals(type, "new") ? R.id.HikingListFragment : R.id.hikingDetailFragment;
                    final NavBackStackEntry backStack = navController.getBackStackEntry(backStackId);
                    backStack.getSavedStateHandle().set("back_result_confirm", true);
                    navController.popBackStack(backStackId, false);
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

    private void setUpDetailView() {
        final HikeInfo hikeInfo = viewModel.getHikeInfoData();
        final String lengthFormat = String.format("%.1fkm", hikeInfo.getLengthOfHike());
        final HikingLevel hikingLevel = HikingLevel.values()[hikeInfo.getLevelOfDifficulty()];
        binding.hikingInfoName.setText(hikeInfo.getNameOfHike());
        binding.hikingInfoLatitude.setText(hikeInfo.getLatitude());
        binding.hikingInfoLongitude.setText(hikeInfo.getLongitude());
        binding.hikingInfoStartDate.setText(InputDateConverter.convertTimeToDateString(hikeInfo.getStartDate()));
        binding.hikingInfoEndDate.setText(InputDateConverter.convertTimeToDateString(hikeInfo.getExpectedDate()));
        binding.hikingInfoLength.setText(lengthFormat);
        binding.hikingInfoLevel.setText(hikingLevel.getLevel());
    }

}