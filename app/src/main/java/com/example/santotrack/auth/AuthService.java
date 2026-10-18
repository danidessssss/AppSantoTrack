package com.example.santotrack.auth;

/**
 * Contrato del servicio de autenticación de SantoTrack.
 * Define las operaciones requeridas independientemente del proveedor subyacente
 * (SQLite local, Firebase Auth, API REST, etc.).
 */
public interface AuthService {
    /**
     * Inicia sesión con correo electrónico y contraseña de forma asíncrona.
     *
     * @param email Correo electrónico ingresado por el usuario.
     * @param password Contraseña ingresada.
     * @param callback Callback que recibe el resultado exitoso o mensaje de error.
     */
    void login(String email, String password, AuthCallback callback);

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param nombre Nombre del estudiante.
     * @param email Correo electrónico.
     * @param password Contraseña a encriptar (MD5 32 caracteres).
     * @param callback Callback de resultado.
     */
    void register(String nombre, String email, String password, AuthCallback callback);

    /**
     * Determina si existe una sesión activa actualmente.
     */
    boolean isUserLoggedIn();

    /**
     * Obtiene el ID del usuario en sesión activa.
     */
    int getCurrentUserId();

    /**
     * Obtiene el nombre del usuario en sesión activa.
     */
    String getCurrentUserName();

    /**
     * Obtiene el correo electrónico del usuario con sesión activa.
     */
    String getCurrentUserEmail();

    /**
     * Cierra la sesión activa del usuario.
     */
    void logout();
}
