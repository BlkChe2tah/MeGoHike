package com.example.megohike.presentation.main_activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.megohike.R;
import com.example.megohike.data.data_source.database.HikeInformationDatabase;
import com.example.megohike.data.use_case.LoadAllHikeInfoUseCaseImpl;
import com.example.megohike.databinding.ActivityMainBinding;
import com.example.megohike.presentation.main_activity.view_model.HikingListViewModel;
import com.example.megohike.presentation.main_activity.view_model.HikingListViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final HikeInformationDatabase database = HikeInformationDatabase.getDatabase(this);
        new ViewModelProvider(this, new HikingListViewModelFactory(new LoadAllHikeInfoUseCaseImpl(database))
        ).get(HikingListViewModel.class);

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        navController.addOnDestinationChangedListener((controller, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.hikingDetailFragment) {
                binding.toolbar.setTitle("");
            } else {
                binding.toolbar.setTitle(navDestination.getLabel());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}