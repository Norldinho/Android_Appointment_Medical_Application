package com.example.mkjli.medcin;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mkjli.MainActivity;
import com.example.mkjli.R;
import com.example.mkjli.databinding.ActivityMainBinding;
import com.example.mkjli.medcin.chat.ChatDoctorFragment;
import com.example.mkjli.medcin.home.HomeDoctorFragment;
import com.example.mkjli.medcin.home.PatientFragment;
import com.example.mkjli.medcin.lorem.LoreamDoctorFragment;
import com.example.mkjli.medcin.lorem.PlaningFragment;
import com.example.mkjli.medcin.profile.ProfileDoctorFragment;
import com.example.mkjli.ui.add.AddFragment;
import com.example.mkjli.ui.chat.ChatFragment;
import com.example.mkjli.ui.home.HomeFragment;
import com.example.mkjli.ui.lorem.LoremFragment;
import com.example.mkjli.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class MainActivity_for_doctor extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_for_doctor);

        bottomNavigationView
                = findViewById(R.id.nav_view2);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.hdoctor);
    }
    HomeDoctorFragment firstFragment = new HomeDoctorFragment();
    PlaningFragment secondFragment = new PlaningFragment();
    ChatDoctorFragment thirdFragment = new ChatDoctorFragment();

    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {

        if (item.getItemId() == R.id.hdoctor) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main_doctor, firstFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.pdoctor) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main_doctor, secondFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.cdoctor) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main_doctor, thirdFragment)
                    .commit();
            return true;
        }
        return false;


    }

    public void gotoPatientFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main_doctor, new PatientFragment())
                .addToBackStack(null) // Ajoutez ceci si vous souhaitez ajouter le fragment à la pile de retour
                .commit();
    }
}