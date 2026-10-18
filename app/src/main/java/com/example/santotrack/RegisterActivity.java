package com.example.santotrack;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.santotrack.auth.AuthCallback;
import com.example.santotrack.auth.AuthManager;
import com.example.santotrack.auth.AuthService;
import com.example.santotrack.auth.AuthValidator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Pantalla de registro de nuevos usuarios para SantoTrack respaldada por SQLite.
 *
 * Validaciones implementadas:
 * - Nombre obligatorio.
 * - Formato de correo válido (ej: alumno@hola.com).
 * - Contraseña de mínimo 6 caracteres con mensaje específico ante longitudes menores.
 * - Encriptación de contraseña en MD5 (VARCHAR(32)).
 */
public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilNombre;
    private TextInputEditText etNombre;
    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private TextInputLayout tilPassword;
    private TextInputEditText etPassword;
    private MaterialButton btnRegister;
    private ProgressBar progressRegister;
    private LinearLayout layoutRegisterError;
    private TextView tvRegisterErrorBanner;
    private TextView tvGoToLogin;

    private AuthService authService;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        authService = AuthManager.getInstance(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupListeners();
    }

    private void initViews() {
        tilNombre = findViewById(R.id.til_register_nombre);
        etNombre = findViewById(R.id.et_register_nombre);
        tilEmail = findViewById(R.id.til_register_email);
        etEmail = findViewById(R.id.et_register_email);
        tilPassword = findViewById(R.id.til_register_password);
        etPassword = findViewById(R.id.et_register_password);
        btnRegister = findViewById(R.id.btn_register);
        progressRegister = findViewById(R.id.progress_register);
        layoutRegisterError = findViewById(R.id.layout_register_error);
        tvRegisterErrorBanner = findViewById(R.id.tv_register_error_banner);
        tvGoToLogin = findViewById(R.id.tv_go_to_login);
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> attemptRegister());

        tvGoToLogin.setOnClickListener(v -> finish());

        etNombre.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tilNombre.getError() != null) tilNombre.setError(null);
                hideErrorBanner();
            }
        });

        etEmail.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tilEmail.getError() != null) tilEmail.setError(null);
                hideErrorBanner();
            }
        });

        etPassword.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tilPassword.getError() != null) tilPassword.setError(null);
                hideErrorBanner();
            }
        });

        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                attemptRegister();
                return true;
            }
            return false;
        });
    }

    private void attemptRegister() {
        if (isLoading) return;

        hideErrorBanner();
        tilNombre.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);

        String nombre = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

        boolean hasError = false;

        if (nombre.isEmpty()) {
            tilNombre.setError(getString(R.string.register_error_empty_name));
            tilNombre.requestFocus();
            hasError = true;
        }

        if (email.isEmpty()) {
            tilEmail.setError(getString(R.string.login_error_empty_email));
            if (!hasError) tilEmail.requestFocus();
            hasError = true;
        } else if (!AuthValidator.isValidEmail(email)) {
            tilEmail.setError(getString(R.string.login_error_invalid_email));
            if (!hasError) tilEmail.requestFocus();
            hasError = true;
        }

        if (password.isEmpty()) {
            tilPassword.setError(getString(R.string.login_error_empty_password));
            if (!hasError) tilPassword.requestFocus();
            hasError = true;
        } else if (password.length() < 6) {
            // Mensaje explícito requerido: se necesitan 6 o más caracteres
            tilPassword.setError(getString(R.string.register_error_password_length));
            if (!hasError) tilPassword.requestFocus();
            hasError = true;
        }

        if (hasError) return;

        setLoadingState(true);

        authService.register(nombre, email, password, new AuthCallback() {
            @Override
            public void onSuccess(String registeredEmail) {
                setLoadingState(false);
                Toast.makeText(RegisterActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
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
            btnRegister.setEnabled(false);
            btnRegister.setText("");
            progressRegister.setVisibility(View.VISIBLE);
            etNombre.setEnabled(false);
            etEmail.setEnabled(false);
            etPassword.setEnabled(false);
        } else {
            btnRegister.setEnabled(true);
            btnRegister.setText(R.string.register_button_text);
            progressRegister.setVisibility(View.GONE);
            etNombre.setEnabled(true);
            etEmail.setEnabled(true);
            etPassword.setEnabled(true);
        }
    }

    private void showErrorBanner(String message) {
        tvRegisterErrorBanner.setText(message != null ? message : getString(R.string.login_error_invalid_credentials));
        layoutRegisterError.setVisibility(View.VISIBLE);
    }

    private void hideErrorBanner() {
        if (layoutRegisterError.getVisibility() == View.VISIBLE) {
            layoutRegisterError.setVisibility(View.GONE);
        }
    }

    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    }
}
