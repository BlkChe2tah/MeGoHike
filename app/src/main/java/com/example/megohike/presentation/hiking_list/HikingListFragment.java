package com.example.megohike.presentation.hiking_list;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.megohike.common.InputTextWatcher;
import com.example.megohike.common.UiStateEmpty;
import com.example.megohike.common.UiStateSearchEmpty;
import com.example.megohike.common.UiStateSuccess;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.data.use_case.LoadAllHikeInfoUseCaseImpl;
import com.example.megohike.databinding.FragmentHikingListBinding;
import com.example.megohike.presentation.main_activity.MainActivity;
import com.example.megohike.presentation.main_activity.view_model.HikingListViewModel;
import com.example.megohike.presentation.main_activity.view_model.HikingListViewModelFactory;
import com.google.gson.Gson;

import java.util.List;

public class HikingListFragment extends Fragment implements HikingInfoViewHolder.OnHikingItemClickListener {

    private FragmentHikingListBinding binding;
    private HikingListViewModel viewModel;
    private HikingInfoAdapter adapter;

    private boolean isEnable = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new HikingInfoAdapter(new HikingInfoAdapter.HikeInfoDiff(), this);
        final HikeInformationDatabase database = HikeInformationDatabase.getDatabase(requireContext());
        viewModel = new ViewModelProvider(requireActivity(), new HikingListViewModelFactory(new LoadAllHikeInfoUseCaseImpl(database))).get(HikingListViewModel.class);
        viewModel.loadHikingList(true);
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentHikingListBinding.inflate(inflater, container, false);
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.contentHikingListEmpty.addNewHikeInfoBtn.setOnClickListener(view -> Navigation.findNavController(view).navigate(
                HikingListFragmentDirections.actionHikingListFragmentToNewHikingFragment(null, "new")
        ));
        binding.fab.setOnClickListener(v -> Navigation.findNavController(v).navigate(
                HikingListFragmentDirections.actionHikingListFragmentToNewHikingFragment(null, "new")
        ));
        binding.advanceSearchBtn.setOnClickListener(v -> {
            isEnable = false;
            Navigation.findNavController(v).navigate(HikingListFragmentDirections.actionHikingListFragmentToAdvanceSearchFragment());
        });
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.searchEditTextList.addTextChangedListener(new InputTextWatcher(s -> {
            if (isEnable) {
                viewModel.setHikeNameByQuickSearch(s);
            }
        }));
        viewModel.uiState.observe(this.getViewLifecycleOwner(), state -> {
            binding.fab.setVisibility(state instanceof UiStateSuccess ? View.VISIBLE : View.GONE);
            binding.recyclerviewFrame.setVisibility(state instanceof UiStateSuccess || state instanceof UiStateSearchEmpty ? View.VISIBLE : View.GONE);
            binding.emptyText.setVisibility(state instanceof UiStateSearchEmpty ? View.VISIBLE : View.GONE);
            binding.contentHikingListEmpty.getRoot().setVisibility(state instanceof UiStateEmpty ? View.VISIBLE : View.GONE);
        });
        viewModel.hikeItems.observe(this.getViewLifecycleOwner(), items -> {
            if (items != null) {
                adapter.submitList(items);
            }
        });
        final NavBackStackEntry backStack = Navigation.findNavController(view).getCurrentBackStackEntry();
        if (backStack != null) {
            backStack.getSavedStateHandle().<Boolean>getLiveData("back_result_confirm").observe(this.getViewLifecycleOwner(), value -> {
                if (value != null && value) {
                    viewModel.loadHikingList(true);
                    backStack.getSavedStateHandle().set("back_result_confirm", null);
                }
            });
            backStack.getSavedStateHandle().<Boolean>getLiveData("back_result").observe(this.getViewLifecycleOwner(), value -> {
                if (value != null && value) {
                    viewModel.loadHikingList(true);
                    backStack.getSavedStateHandle().set("back_result", null);
                }
            });
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (viewModel.getHikeName() != null) {
            binding.searchEditTextList.setText(viewModel.getHikeName(), TextView.BufferType.EDITABLE);
            isEnable = true;
            viewModel.loadHikingList(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v, HikeInfo data) {
        Navigation.findNavController(v).navigate(
                HikingListFragmentDirections.actionHikingListFragmentToHikingDetailFragment(Integer.toString(data.getHikeInfoId()))
        );
    }
}