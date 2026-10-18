package com.example.santotrack.auth;

/**
 * Interfaz de callback asíncrono para operaciones de autenticación.
 */
public interface AuthCallback {
    void onSuccess(String email);
    void onError(String errorMessage);
}
