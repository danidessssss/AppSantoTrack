package com.example.santotrack;

import com.example.santotrack.auth.AuthValidator;
import com.example.santotrack.util.HashUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Pruebas unitarias para la validación de credenciales y utilidades criptográficas en SantoTrack.
 */
public class AuthValidatorTest {

    @Test
    public void testValidEmails() {
        assertTrue(AuthValidator.isValidEmail("alumno@hola.com"));
        assertTrue(AuthValidator.isValidEmail("estudiante@santotrack.cl"));
        assertTrue(AuthValidator.isValidEmail("alumno@alumnos.santotomas.cl"));
        assertTrue(AuthValidator.isValidEmail("usuario.prueba@gmail.com"));
        assertTrue(AuthValidator.isValidEmail("test+tag@dominio.edu"));
        assertTrue(AuthValidator.isValidEmail("  espacios.trim@dominio.com  "));
    }

    @Test
    public void testInvalidEmails() {
        assertFalse(AuthValidator.isValidEmail(null));
        assertFalse(AuthValidator.isValidEmail(""));
        assertFalse(AuthValidator.isValidEmail("   "));
        assertFalse(AuthValidator.isValidEmail("correo-sin-arroba.com"));
        assertFalse(AuthValidator.isValidEmail("@dominio.com"));
        assertFalse(AuthValidator.isValidEmail("usuario@"));
        assertFalse(AuthValidator.isValidEmail("usuario@dominio"));
    }

    @Test
    public void testValidPasswords() {
        assertTrue(AuthValidator.isValidPassword("123456"));
        assertTrue(AuthValidator.isValidPassword("password123"));
        assertTrue(AuthValidator.isValidPassword("ClaveSegura#2025"));
    }

    @Test
    public void testInvalidPasswords() {
        assertFalse(AuthValidator.isValidPassword(null));
        assertFalse(AuthValidator.isValidPassword(""));
        assertFalse(AuthValidator.isValidPassword("12345"));
        assertFalse(AuthValidator.isValidPassword("a"));
    }

    @Test
    public void testMd5HashLengthAndConsistency() {
        String hash = HashUtil.md5("123456");
        assertNotNull(hash);
        // Debe tener exactamente 32 caracteres para encajar en VARCHAR(32)
        assertEquals(32, hash.length());
        // Coincidencia con hash MD5 conocido de "123456"
        assertEquals("e10adc3949ba59abbe56e057f20f883e", hash);

        // Claves distintas generan hashes distintos
        String hash2 = HashUtil.md5("654321");
        assertEquals(32, hash2.length());
        assertNotEquals(hash, hash2);
    }
}
