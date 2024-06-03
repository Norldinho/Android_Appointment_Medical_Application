package com.example.mkjli;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mkjli.ui.add.AddFragment;
import com.example.mkjli.ui.add.DoctorDonnee;
import com.example.mkjli.ui.chat.ChatFragment;
import com.example.mkjli.ui.home.HomeFragment;
import com.example.mkjli.ui.lorem.LoremFragment;
import com.example.mkjli.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mkjli.databinding.ActivityMainBinding;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_rdv, R.id.navigation_add, R.id.navigation_chat, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }


    // Dans votre MainActivity
    public void goToResultFragment(List<DoctorDonnee> donnerDoctorList) {
        ResultFragment resultFragment = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("doctorList", (Serializable) donnerDoctorList);
        resultFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, resultFragment)
                .addToBackStack(null)
                .commit();
    }


}
