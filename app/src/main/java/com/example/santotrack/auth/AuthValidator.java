package com.example.santotrack.auth;

import java.util.regex.Pattern;

/**
 * Validador de credenciales y campos de autenticación para SantoTrack.
 * Encapsula las reglas de formato para permitir pruebas unitarias limpias.
 */
public class AuthValidator {

    // Expresión regular estándar para validación de formato de correo electrónico
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final int MIN_PASSWORD_LENGTH = 6;

    /**
     * Valida que el correo no sea nulo ni vacío y cumpla con el formato estándar.
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String trimmed = email.trim();
        return !trimmed.isEmpty() && EMAIL_PATTERN.matcher(trimmed).matches();
    }

    /**
     * Valida que la contraseña cumpla con los requisitos mínimos de longitud.
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= MIN_PASSWORD_LENGTH;
    }

    public static int getMinPasswordLength() {
        return MIN_PASSWORD_LENGTH;
    }
}
