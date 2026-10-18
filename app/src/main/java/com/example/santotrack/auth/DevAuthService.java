package com.example.santotrack.auth;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;

/**
 * IMPLEMENTACIÓN TEMPORAL DE DESARROLLO (MOCK)
 * ============================================================================
 * Esta clase es una implementación provisoria para validar el flujo completo
 * de la interfaz de usuario de Login mientras se configura un backend o servicio
 * de autenticación definitivo (como Firebase Authentication o API REST institucional).
 *
 * CUMPLIMIENTO DE SEGURIDAD Y REGLAS DE ARQUITECTURA:
 * 1. NO guarda contraseñas en texto plano ni en SharedPreferences.
 * 2. NO utiliza una contraseña maestra fija como sustituto de autenticación real.
 * 3. Valida campos vacíos, formato RFC de correo electrónico y longitud de contraseña.
 * 4. Introduce una latencia asíncrona simulada (1000 ms) para verificar:
 *    - La animación de carga (ProgressBar).
 *    - La deshabilitación de controles para evitar solicitudes concurrentes.
 * 5. Permite simular credenciales incorrectas usando el correo: "error@santotrack.cl"
 *    o una contraseña menor a 6 caracteres.
 * ============================================================================
 */
public class DevAuthService implements AuthService {

    private final SessionManager sessionManager;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public DevAuthService(Context context) {
        this.sessionManager = new SessionManager(context);
    }

    @Override
    public void login(String email, String password, AuthCallback callback) {
        if (callback == null) return;

        // Validación de correo vacío
        if (email == null || email.trim().isEmpty()) {
            callback.onError("Por favor ingresa tu correo electrónico.");
            return;
        }

        final String trimmedEmail = email.trim();

        // Validación de formato de correo
        if (!AuthValidator.isValidEmail(trimmedEmail)) {
            callback.onError("Ingresa un formato de correo válido (ejemplo@dominio.com).");
            return;
        }

        // Validación de contraseña vacía
        if (password == null || password.isEmpty()) {
            callback.onError("Por favor ingresa tu contraseña.");
            return;
        }

        // Validación de longitud mínima
        if (!AuthValidator.isValidPassword(password)) {
            callback.onError("La contraseña debe tener al menos " + AuthValidator.getMinPasswordLength() + " caracteres.");
            return;
        }

        // Simulación de latencia asíncrona de red (1 segundo)
        mainHandler.postDelayed(() -> {
            // Caso de prueba para simular error de credenciales
            if (trimmedEmail.equalsIgnoreCase("error@santotrack.cl") || password.equals("error123")) {
                callback.onError("Credenciales incorrectas. Verifica tu correo y contraseña.");
                return;
            }

            // En entorno de desarrollo temporal:
            // Cualquier correo válido y contraseña de 6+ caracteres es aceptado para probar la interfaz.
            // La sesión guarda únicamente el correo y la bandera de autenticación, NUNCA la contraseña.
            sessionManager.saveSession(trimmedEmail);
            callback.onSuccess(trimmedEmail);
        }, 1000);
    }

    @Override
    public void register(String nombre, String email, String password, AuthCallback callback) {
        login(email, password, callback);
    }

    @Override
    public boolean isUserLoggedIn() {
        return sessionManager.isLoggedIn();
    }

    @Override
    public int getCurrentUserId() {
        return sessionManager.getUserId();
    }

    @Override
    public String getCurrentUserName() {
        return sessionManager.getUserName();
    }

    @Override
    public String getCurrentUserEmail() {
        return sessionManager.getUserEmail();
    }

    @Override
    public void logout() {
        sessionManager.clearSession();
    }
}
