package com.example.santotrack.auth;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.example.santotrack.db.UsuarioDao;
import com.example.santotrack.model.Usuario;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Servicio de autenticación respaldado por SQLite local para SantoTrack.
 * Cumple con el modelo relacional:
 * - Tabla USUARIO
 * - Contraseña encriptada en MD5 (VARCHAR(32))
 * - Persistencia segura de sesión en SessionManager (sin almacenar contraseñas).
 */
public class SqliteAuthService implements AuthService {

    private final UsuarioDao usuarioDao;
    private final SessionManager sessionManager;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public SqliteAuthService(Context context) {
        this.usuarioDao = new UsuarioDao(context);
        this.sessionManager = new SessionManager(context);
    }

    @Override
    public void login(String email, String password, AuthCallback callback) {
        if (callback == null) return;

        if (email == null || email.trim().isEmpty()) {
            callback.onError("Por favor ingresa tu correo electrónico.");
            return;
        }

        final String trimmedEmail = email.trim();
        if (!AuthValidator.isValidEmail(trimmedEmail)) {
            callback.onError("Ingresa un formato de correo válido (ej: alumno@hola.com).");
            return;
        }

        if (password == null || password.isEmpty()) {
            callback.onError("Por favor ingresa tu contraseña.");
            return;
        }

        if (!AuthValidator.isValidPassword(password)) {
            callback.onError("Se necesitan 6 o más caracteres para la contraseña.");
            return;
        }

        executor.execute(() -> {
            try {
                // Pequeña pausa para simular proceso y permitir render de spinner
                Thread.sleep(400);

                Usuario usuario = usuarioDao.login(trimmedEmail, password);
                mainHandler.post(() -> {
                    if (usuario != null) {
                        sessionManager.saveSession(usuario.getIdUsuario(), usuario.getNombre(), usuario.getCorreo());
                        callback.onSuccess(usuario.getCorreo());
                    } else {
                        callback.onError("Credenciales incorrectas. Verifica tu correo y contraseña.");
                    }
                });
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError("Error de base de datos: " + e.getMessage()));
            }
        });
    }

    @Override
    public void register(String nombre, String email, String password, AuthCallback callback) {
        if (callback == null) return;

        if (nombre == null || nombre.trim().isEmpty()) {
            callback.onError("Por favor ingresa tu nombre completo.");
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            callback.onError("Por favor ingresa tu correo electrónico.");
            return;
        }

        final String trimmedEmail = email.trim();
        if (!AuthValidator.isValidEmail(trimmedEmail)) {
            callback.onError("Ingresa un formato de correo válido (ej: alumno@hola.com).");
            return;
        }

        if (password == null || password.isEmpty()) {
            callback.onError("Por favor ingresa tu contraseña.");
            return;
        }

        if (!AuthValidator.isValidPassword(password)) {
            callback.onError("Se necesitan 6 o más caracteres para la contraseña.");
            return;
        }

        executor.execute(() -> {
            try {
                Thread.sleep(400);

                if (usuarioDao.existeCorreo(trimmedEmail)) {
                    mainHandler.post(() -> callback.onError("Este correo electrónico ya se encuentra registrado."));
                    return;
                }

                long idUsuario = usuarioDao.registrar(nombre.trim(), trimmedEmail, password);
                mainHandler.post(() -> {
                    if (idUsuario > 0) {
                        sessionManager.saveSession((int) idUsuario, nombre.trim(), trimmedEmail);
                        callback.onSuccess(trimmedEmail);
                    } else {
                        callback.onError("No fue posible registrar el usuario. Inténtalo de nuevo.");
                    }
                });
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError("Error al registrar en SQLite: " + e.getMessage()));
            }
        });
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
