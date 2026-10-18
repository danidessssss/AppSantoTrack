package com.example.santotrack;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.santotrack.auth.AuthCallback;
import com.example.santotrack.auth.AuthManager;
import com.example.santotrack.auth.AuthService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Actividad de inicio de sesión (Login) para SantoTrack.
 *
 * Flujo y control:
 * - Valida formato de correo y longitud de contraseña.
 * - Muestra indicadores visuales de progreso (ProgressBar).
 * - Bloquea botones para evitar múltiples peticiones concurrentes.
 * - Tras autenticación exitosa, redirige a MainActivity y limpia el historial
 *   (Back Stack) para que el botón Atrás no retorne al Login.
 */
public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private TextInputLayout tilPassword;
    private TextInputEditText etPassword;
    private MaterialButton btnLogin;
    private ProgressBar progressLogin;
    private LinearLayout layoutErrorBanner;
    private TextView tvErrorBanner;

    private AuthService authService;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authService = AuthManager.getInstance(this);

        // Si ya existe una sesión activa, acceder directamente a MainActivity
        if (authService.isUserLoggedIn()) {
            navigateToMainActivity();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupListeners();
        setupBackNavigation();
    }

    private void initViews() {
        tilEmail = findViewById(R.id.til_email);
        etEmail = findViewById(R.id.et_email);
        tilPassword = findViewById(R.id.til_password);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        progressLogin = findViewById(R.id.progress_login);
        layoutErrorBanner = findViewById(R.id.layout_error_banner);
        tvErrorBanner = findViewById(R.id.tv_error_banner);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());

        TextView tvGoToRegister = findViewById(R.id.tv_go_to_register);
        if (tvGoToRegister != null) {
            tvGoToRegister.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            });
        }

        // Limpieza reactiva de errores al modificar el texto
        etEmail.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tilEmail.getError() != null) {
                    tilEmail.setError(null);
                }
                hideErrorBanner();
            }
        });

        etPassword.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tilPassword.getError() != null) {
                    tilPassword.setError(null);
                }
                hideErrorBanner();
            }
        });

        // Soporte para tecla Enter/Done del teclado virtual
        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                attemptLogin();
                return true;
            }
            return false;
        });
    }

    private void setupBackNavigation() {
        // En la pantalla inicial de Login, la acción Atrás sale de la aplicación de forma limpia
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!isLoading) {
                    finish();
                }
            }
        });
    }

    private void attemptLogin() {
        if (isLoading) {
            return; // Prevenir peticiones simultáneas concurrentes
        }

        hideErrorBanner();
        tilEmail.setError(null);
        tilPassword.setError(null);

        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

        // Validaciones del formulario
        boolean hasError = false;

        if (email.isEmpty()) {
            tilEmail.setError(getString(R.string.login_error_empty_email));
            tilEmail.requestFocus();
            hasError = true;
        } else if (!com.example.santotrack.auth.AuthValidator.isValidEmail(email)) {
            tilEmail.setError(getString(R.string.login_error_invalid_email));
            tilEmail.requestFocus();
            hasError = true;
        }

        if (password.isEmpty()) {
            tilPassword.setError(getString(R.string.login_error_empty_password));
            if (!hasError) {
                tilPassword.requestFocus();
            }
            hasError = true;
        } else if (!com.example.santotrack.auth.AuthValidator.isValidPassword(password)) {
            tilPassword.setError(getString(R.string.login_error_short_password));
            if (!hasError) {
                tilPassword.requestFocus();
            }
            hasError = true;
        }

        if (hasError) {
            return;
        }

        // Ejecución de la solicitud de autenticación
        setLoadingState(true);

        authService.login(email, password, new AuthCallback() {
            @Override
            public void onSuccess(String authenticatedEmail) {
                setLoadingState(false);
                navigateToMainActivity();
            }

            @Override
            public void onError(String errorMessage) {
                setLoadingState(false);
                showErrorBanner(errorMessage);
            }
        });
    }

    private void setLoadingState(boolean loading) {
        this.isLoading = loading;
        if (loading) {
            btnLogin.setEnabled(false);
            btnLogin.setText("");
            progressLogin.setVisibility(View.VISIBLE);
            etEmail.setEnabled(false);
            etPassword.setEnabled(false);
        } else {
            btnLogin.setEnabled(true);
            btnLogin.setText(R.string.login_button_text);
            progressLogin.setVisibility(View.GONE);
            etEmail.setEnabled(true);
            etPassword.setEnabled(true);
        }
    }

    private void showErrorBanner(String message) {
        tvErrorBanner.setText(message != null ? message : getString(R.string.login_error_invalid_credentials));
        layoutErrorBanner.setVisibility(View.VISIBLE);
    }

    private void hideErrorBanner() {
        if (layoutErrorBanner.getVisibility() == View.VISIBLE) {
            layoutErrorBanner.setVisibility(View.GONE);
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        // Limpia la pila de navegación para impedir que el usuario regrese al Login con 'Atrás'
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    }
}
