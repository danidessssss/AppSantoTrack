package com.example.santotrack.auth;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Gestor de persistencia de sesión de usuario para SantoTrack.
 *
 * NOTA DE SEGURIDAD:
 * Solo persiste banderas de sesión y los identificadores públicos (id_usuario, nombre y correo).
 * NUNCA almacena contraseñas en texto plano ni información sensible.
 */
public class SessionManager {
    private static final String PREF_NAME = "santotrack_session_prefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        this.prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Guarda el estado de sesión activa para el usuario.
     */
    public void saveSession(int userId, String name, String email) {
        prefs.edit()
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .putInt(KEY_USER_ID, userId)
                .putString(KEY_USER_NAME, name)
                .putString(KEY_USER_EMAIL, email)
                .apply();
    }

    /**
     * Compatibilidad con versión anterior.
     */
    public void saveSession(String email) {
        saveSession(1, "Estudiante", email);
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "Estudiante");
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, null);
    }

    public void clearSession() {
        prefs.edit()
                .remove(KEY_IS_LOGGED_IN)
                .remove(KEY_USER_ID)
                .remove(KEY_USER_NAME)
                .remove(KEY_USER_EMAIL)
                .apply();
    }
}
