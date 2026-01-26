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

/**
 * Activity principal da aplicação.
 * Gere a navegação entre os três fragments principais: Dashboard, Lista e Mapa.
 */
public class MainActivity extends AppCompatActivity {

    // Cache de fragments para evitar recriação desnecessária
    private final DashboardFragment dashboardFragment = new DashboardFragment();
    private final ParkingListFragment parkingListFragment = new ParkingListFragment();
    private final MapFragment mapFragment = new MapFragment();

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupBottomNavigation();

        // Mostrar Dashboard por defeito
        if (savedInstanceState == null) {
            showFragment(dashboardFragment);
        }
    }

    /**
     * Configura a toolbar com o menu de logout.
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.topToolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_logout) {
                performLogout();
                return true;
            }
            return false;
        });
    }

    /**
     * Configura a navegação inferior.
     */
    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_dashboard) {
                showFragment(dashboardFragment);
            } else if (itemId == R.id.nav_list) {
                showFragment(parkingListFragment);
            } else if (itemId == R.id.nav_map) {
                showFragment(mapFragment);
            }

            return true;
        });
    }

    /**
     * Mostra um fragment no container principal.
     * Usa hide/show para preservar estado dos fragments.
     *
     * @param fragment Fragment a mostrar
     */
    private void showFragment(@NonNull Fragment fragment) {
        if (currentFragment == fragment) {
            return; // Já está visível
        }

        var transaction = getSupportFragmentManager().beginTransaction();

        // Esconder fragment atual se existir
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }

        // Adicionar ou mostrar o novo fragment
        if (!fragment.isAdded()) {
            transaction.add(R.id.fragmentContainer, fragment);
        } else {
            transaction.show(fragment);
        }

        transaction.commit();
        currentFragment = fragment;
    }

    /**
     * Executa o logout do utilizador e redireciona para o login.
     */
    private void performLogout() {
        new SessionManager(this).logout();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
