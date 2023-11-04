package com.example.megohike.presentation.hiking_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.megohike.MainActivity;
import com.example.megohike.R;
import com.example.megohike.common.InputDateConverter;
import com.example.megohike.data.data_source.database.entities.HikeInfo;
import com.example.megohike.data.data_source.database.entities.Observation;
import com.example.megohike.databinding.FragmentHikingDetailBinding;
import com.example.megohike.domain.HikingLevel;
import com.example.megohike.presentation.hiking_list.HikingInfoAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HikingDetailFragment extends Fragment implements ObservationInfoViewHolder.OnObservationItemClickListener {
    private FragmentHikingDetailBinding binding;
    private HikeInfo hikeInfo;

    private ObservationInfoAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final MaterialToolbar toolbar = ((MainActivity)getContext()).findViewById(R.id.toolbar);
        toolbar.setTitle("");
        adapter = new ObservationInfoAdapter(new ObservationInfoAdapter.ObservationDiff(), this);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentHikingDetailBinding.inflate(inflater, container, false);
        binding.hikingInfoObservation.setAdapter(adapter);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Gson gson = new Gson();
        String string = HikingDetailFragmentArgs.fromBundle(getArguments()).getHikeInfo();
        hikeInfo = gson.fromJson(string, HikeInfo.class);
        setUpDetailView();
        final List<Observation> items = new ArrayList<>(1);
//        items.add(new Observation(0, 0, "Yangon", "", 0, "yangon"));
//        items.add(new Observation(0, 0, "Yangon", "", 0, "yangon"));
//        items.add(new Observation(0, 0, "Yangon", "", 0, "yangon"));
//        items.add(new Observation(0, 0, "Yangon", "", 0, "yangon"));
        items.add(null);
        adapter.submitList(items);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setUpDetailView() {
        final String parkingAvailableResult = hikeInfo.getParkingAvailable() == 0 ? "No" : "Yes";
        final String lengthFormat = String.format("%.1fkm", hikeInfo.getLengthOfHike());
        final HikingLevel hikingLevel = HikingLevel.values()[hikeInfo.getLevelOfDifficulty()];
        binding.hikingInfoName.setText(hikeInfo.getNameOfHike());
        binding.hikingInfoLength.setText(lengthFormat);
        binding.hikingInfoLevel.setText(hikingLevel.getLevel());
        binding.hikingInfoParkingAvailable.setText(parkingAvailableResult);
        // detail
        binding.hikingInfoLatitude.setText(hikeInfo.getLatitude());
        binding.hikingInfoLongitude.setText(hikeInfo.getLongitude());
        binding.hikingInfoStartDate.setText(InputDateConverter.convertTimeToDateString(hikeInfo.getStartDate()));
        binding.hikingInfoEndDate.setText(InputDateConverter.convertTimeToDateString(hikeInfo.getExpectedDate()));
    }

    @Override
    public void onClick(View view, Observation data) {
    }

    @Override
    public void onAddNewItemClick(View view) {
        Navigation.findNavController(view).navigate(
                HikingDetailFragmentDirections.actionHikingDetailFragmentToNewObservationFragment()
        );
    }
}