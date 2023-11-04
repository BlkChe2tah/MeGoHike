package com.example.megohike.presentation.hiking_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.megohike.R;
import com.example.megohike.common.UiStateEmpty;
import com.example.megohike.common.UiStateSuccess;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.data.use_case.LoadAllHikeInfoUseCaseImpl;
import com.example.megohike.databinding.FragmentHikingListBinding;
import com.example.megohike.presentation.hiking_list.view_model.HikingListViewModel;
import com.example.megohike.presentation.hiking_list.view_model.HikingListViewModelFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class HikingListFragment extends Fragment implements HikingInfoViewHolder.OnHikingItemClickListener {

    private FragmentHikingListBinding binding;
    private HikingListViewModel viewModel;

    private HikingInfoAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new HikingInfoAdapter(new HikingInfoAdapter.HikeInfoDiff(), this);
        final HikeInformationDatabase database = HikeInformationDatabase.getDatabase(getContext());
        viewModel = new ViewModelProvider(
                this, new HikingListViewModelFactory(new LoadAllHikeInfoUseCaseImpl(database))
        ).get(HikingListViewModel.class);
        viewModel.loadHikingList();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentHikingListBinding.inflate(inflater, container, false);
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.contentHikingListEmpty.addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(
                        HikingListFragmentDirections.actionHikingListFragmentToNewHikingFragment()
                );
            }
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(
                        HikingListFragmentDirections.actionHikingListFragmentToNewHikingFragment()
                );
            }
        });
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getUiState().observe(this.getViewLifecycleOwner(), state -> {
            binding.fab.setVisibility(state instanceof UiStateSuccess<List<HikeInfo>> ? View.VISIBLE : View.GONE);
            binding.recyclerview.setVisibility(state instanceof UiStateSuccess<List<HikeInfo>> ? View.VISIBLE : View.GONE);
            binding.contentHikingListEmpty.getRoot().setVisibility(state instanceof UiStateEmpty<List<HikeInfo>> ? View.VISIBLE : View.GONE);
            if (state instanceof UiStateSuccess<List<HikeInfo>>) {
                adapter.submitList(((UiStateSuccess<List<HikeInfo>>) state).getData());
            }
        });
        final NavBackStackEntry backStack = Navigation.findNavController(view).getCurrentBackStackEntry();
        if (backStack != null) {
            backStack.getSavedStateHandle().<Boolean>getLiveData("back_result").observe(this.getViewLifecycleOwner(), value -> {
                if (value != null && value) {
                    viewModel.loadHikingList();
                    backStack.getSavedStateHandle().set("back_result", null);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v, HikeInfo data) {
        final Gson gson = new Gson();
        Navigation.findNavController(v).navigate(
                HikingListFragmentDirections.actionHikingListFragmentToHikingDetailFragment(gson.toJson(data))
        );
    }
}