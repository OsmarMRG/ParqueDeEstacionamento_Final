package com.example.parquedeestacionamento.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.parquedeestacionamento.R;
import com.example.parquedeestacionamento.fragments.DashboardFragment;
import com.example.parquedeestacionamento.fragments.MapFragment;
import com.example.parquedeestacionamento.fragments.ParkingListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private void show(Fragment f) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, f)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView nav = findViewById(R.id.bottomNav);

        show(new DashboardFragment());

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_dashboard) show(new DashboardFragment());
            else if (id == R.id.nav_list) show(new ParkingListFragment());
            else if (id == R.id.nav_map) show(new MapFragment());
            return true;
        });
    }
}
