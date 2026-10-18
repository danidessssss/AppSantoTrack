package com.example.santotrack.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad criptográfica para SantoTrack.
 * Genera un resumen MD5 de longitud fija de 32 caracteres hexadecimales,
 * correspondiente exactamente a la columna VARCHAR(32) de la tabla USUARIO.
 */
public class HashUtil {

    /**
     * Calcula el hash MD5 de una cadena de texto.
     *
     * @param input Texto a encriptar/hashear.
     * @return Cadena de 32 caracteres hexadecimales en minúsculas.
     */
    public static String md5(String input) {
        if (input == null) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(32);
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al instanciar algoritmo MD5", e);
        }
    }
}
