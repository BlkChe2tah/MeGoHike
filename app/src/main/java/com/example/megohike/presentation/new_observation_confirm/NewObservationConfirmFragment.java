package com.example.megohike.presentation.new_observation_confirm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.megohike.common.InputDateConverter;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.data.data_source.database.entities.Observation;
import com.example.megohike.data.use_case.AddNewHikeInfoUseCaseImpl;
import com.example.megohike.data.use_case.AddNewObservationUseCaseImpl;
import com.example.megohike.databinding.FragmentNewHikingConfirmBinding;
import com.example.megohike.databinding.FragmentNewObservationConfirmBinding;
import com.example.megohike.domain.HikingLevel;
import com.example.megohike.presentation.new_hiking.NewHikingFragmentArgs;
import com.example.megohike.presentation.new_hiking_confirm.NewHikingConfirmFragmentArgs;
import com.example.megohike.presentation.new_hiking_confirm.view_model.NewHikingConfirmViewModel;
import com.example.megohike.presentation.new_hiking_confirm.view_model.NewHikingConfirmViewModelFactory;
import com.example.megohike.presentation.new_observation_confirm.view_model.NewObservationConfirmViewModel;
import com.example.megohike.presentation.new_observation_confirm.view_model.NewObservationConfirmViewModelFactory;
import com.google.gson.Gson;

import java.util.Objects;

public class NewObservationConfirmFragment extends Fragment {
    private FragmentNewObservationConfirmBinding binding;
    private NewObservationConfirmViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final HikeInformationDatabase database = HikeInformationDatabase.getDatabase(getContext());
        viewModel = new ViewModelProvider(
                this, new NewObservationConfirmViewModelFactory(
                new AddNewObservationUseCaseImpl(database))
        ).get(NewObservationConfirmViewModel.class);
        final Gson gson = new Gson();
        String string = NewObservationConfirmFragmentArgs.fromBundle(getArguments()).getObservation();
        viewModel.setObservationData(gson.fromJson(string, Observation.class));
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentNewObservationConfirmBinding.inflate(inflater, container, false);
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
                    final NavBackStackEntry backStack = navController.getBackStackEntry(R.id.hikingDetailFragment);
                    backStack.getSavedStateHandle().set("back_result", true);
                    navController.popBackStack(R.id.hikingDetailFragment, false);
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
        final Observation observation = viewModel.getObservationData();
        binding.obName.setText(observation.getObservation());
        binding.obCommentFrame.setVisibility(observation.getComment().isEmpty() ? View.GONE : View.VISIBLE);
        binding.obComment.setText(observation.getComment());
        binding.obDate.setText(InputDateConverter.getDate(observation.getTime()));
        binding.obTime.setText(InputDateConverter.getTime(observation.getTime()));
    }

}