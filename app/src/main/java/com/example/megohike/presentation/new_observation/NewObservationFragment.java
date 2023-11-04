package com.example.megohike.presentation.new_observation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.megohike.databinding.FragmentNewObservationBinding;

public class NewObservationFragment extends Fragment {

    private FragmentNewObservationBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewObservationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}