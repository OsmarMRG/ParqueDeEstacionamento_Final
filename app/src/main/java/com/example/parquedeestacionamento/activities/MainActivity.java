package com.example.parquedeestacionamento.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.parquedeestacionamento.R;
import com.example.parquedeestacionamento.fragments.DashboardFragment;
import com.example.parquedeestacionamento.fragments.MapFragment;
import com.example.parquedeestacionamento.fragments.ParkingListFragment;
import com.example.parquedeestacionamento.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

// Activity principal: gere os três fragmentos (Dashboard, Histórico, Mapa)
public class MainActivity extends AppCompatActivity {

    private final DashboardFragment dashboardFragment = new DashboardFragment();
    private final ParkingListFragment parkingListFragment = new ParkingListFragment();
    private final MapFragment mapFragment = new MapFragment();
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar com botão de logout
        Toolbar toolbar = findViewById(R.id.topToolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_logout) {
                new SessionManager(this).logout();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });

        // Navegação inferior entre fragmentos
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_dashboard) showFragment(dashboardFragment);
            else if (id == R.id.nav_list) showFragment(parkingListFragment);
            else if (id == R.id.nav_map) showFragment(mapFragment);
            return true;
        });

        // Mostrar dashboard por defeito
        if (savedInstanceState == null) showFragment(dashboardFragment);
    }

    // Mostra um fragmento, escondendo o anterior
    private void showFragment(@NonNull Fragment fragment) {
        if (currentFragment == fragment) return;
        var transaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) transaction.hide(currentFragment);
        if (!fragment.isAdded()) transaction.add(R.id.fragmentContainer, fragment);
        else transaction.show(fragment);
        transaction.commit();
        currentFragment = fragment;
    }
}
