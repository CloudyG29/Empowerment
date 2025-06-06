package com.nullandvoid.empowerment;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.nullandvoid.empowerment.databinding.ActivityMenuBinding;

public class Menu extends AppCompatActivity {
    public static ProgressBar loader;
    private ActivityMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_empower,
                R.id.navigation_beEmpowered,
                R.id.navigation_profile,
                R.id.navigation_leaderboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_menu);
        NavigationUI.setupWithNavController(binding.navView, navController);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            showProgressBar();
        });

        navView.setOnItemSelectedListener(item -> {
            NavDestination curr = navController.getCurrentDestination();
            if (curr != null && curr.getId() == item.getItemId()) {
                hideProgressBar();
                return true;
            }

            showProgressBar();
            NavigationUI.onNavDestinationSelected(item, navController);
            return true;
        });

        loader = findViewById(R.id.progressBar2);
    }

    public static void showProgressBar() {
        if (loader != null) {
            loader.setVisibility(View.VISIBLE);
        }
    }

    public static void hideProgressBar() {
        if (loader != null) {
            loader.setVisibility(View.GONE);
        }
    }


}