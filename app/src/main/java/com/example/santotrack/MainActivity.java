package com.example.santotrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.example.santotrack.auth.AuthManager;
import com.example.santotrack.auth.AuthService;
import com.example.santotrack.ui.AsignaturasFragment;
import com.example.santotrack.ui.CalculadoraFragment;
import com.example.santotrack.ui.NuevaMateriaFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verificación de seguridad de sesión activa
        authService = AuthManager.getInstance(this);
        if (!authService.isUserLoggedIn()) {
            redirectToLogin();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.nav_asignaturas) {
                    selectedFragment = new AsignaturasFragment();
                } else if (itemId == R.id.nav_calculadora) {
                    selectedFragment = new CalculadoraFragment();
                } else if (itemId == R.id.nav_nueva_materia) {
                    selectedFragment = new NuevaMateriaFragment();
                }
                
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true;
                }
                return false;
            }
        });

        // Set default selection
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_asignaturas);
        }

        View ivProfile = findViewById(R.id.iv_profile_header);
        if (ivProfile != null) {
            ivProfile.setOnClickListener(v -> showLogoutDialog());
        }
    }

    public void navigateToTab(int itemId) {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(itemId);
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showLogoutDialog() {
        String email = authService != null ? authService.getCurrentUserEmail() : "";
        String message = email != null && !email.isEmpty()
                ? getString(R.string.dialog_logout_message, email)
                : "¿Deseas cerrar la sesión activa?";

        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_logout_title)
                .setMessage(message)
                .setPositiveButton(R.string.action_logout, (dialog, which) -> {
                    if (authService != null) {
                        authService.logout();
                    }
                    redirectToLogin();
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }
}