package com.example.parquedeestacionamento.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.parquedeestacionamento.R;
import com.example.parquedeestacionamento.fragments.DashboardFragment;
import com.example.parquedeestacionamento.fragments.MapFragment;
import com.example.parquedeestacionamento.fragments.ParkingListFragment;
import com.example.parquedeestacionamento.utils.SessionManager;
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

        // Toolbar "falsa" (sem ActionBar) só para o ícone de logout
        Toolbar toolbar = findViewById(R.id.topToolbar);

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_logout) {
                doLogout();
                return true;
            }
            return false;
        });

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

    private void doLogout() {
        new SessionManager(this).logout();

        Intent i = new Intent(this, LoginPage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}
