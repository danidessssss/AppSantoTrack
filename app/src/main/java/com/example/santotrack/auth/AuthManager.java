package com.example.santotrack.auth;

import android.content.Context;

/**
 * Proveedor único de acceso a AuthService para la aplicación SantoTrack.
 * Permite desacoplar las actividades de la implementación concreta de autenticación.
 */
public class AuthManager {
    private static AuthService instance;

    public static synchronized AuthService getInstance(Context context) {
        if (instance == null) {
            // Implementación con SQLite local y contraseñas hasheadas en MD5 (VARCHAR(32))
            instance = new SqliteAuthService(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Permite inyectar una implementación personalizada (útil para tests unitarios
     * o integración con Firebase).
     */
    public static synchronized void setInstance(AuthService authService) {
        instance = authService;
    }
}
