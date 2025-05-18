package com.nullandvoid.empowerment;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.nullandvoid.empowerment.databinding.ActivityMenuBinding;
import com.nullandvoid.empowerment.ui.home.HomeFragment;
import com.nullandvoid.empowerment.ui.item_select;

public class Menu extends AppCompatActivity {

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
        binding.navView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_beEmpowered:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new item_select())
                            .commit();
                    return true;



                default:
                    return false;
            }
        });

    }


}