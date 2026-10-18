package com.example.santotrack.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.santotrack.model.Usuario;
import com.example.santotrack.util.HashUtil;

/**
 * Data Access Object (DAO) para la tabla USUARIO en SQLite.
 */
public class UsuarioDao {

    private final DatabaseHelper dbHelper;

    public UsuarioDao(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Registra un nuevo usuario en la base de datos SQLite.
     * Encripta la contraseña usando MD5 generando exactamente 32 caracteres (VARCHAR(32)).
     *
     * @param nombre Nombre del usuario.
     * @param correo Correo electrónico (identificador único).
     * @param contrasenaPlana Contraseña en texto plano a hashear.
     * @return id_usuario generado, o -1 en caso de fallo.
     */
    public long registrar(String nombre, String correo, String contrasenaPlana) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String contrasenaHasheada = HashUtil.md5(contrasenaPlana);

        ContentValues values = new ContentValues();
        values.put("nombre", nombre.trim());
        values.put("correo", correo.trim().toLowerCase());
        values.put("contrasena", contrasenaHasheada);

        return db.insert("USUARIO", null, values);
    }

    /**
     * Valida las credenciales de inicio de sesión contra SQLite.
     * Hashea la contraseña ingresada y la compara con la almacenada en la base de datos.
     *
     * @param correo Correo ingresado.
     * @param contrasenaPlana Contraseña en texto plano ingresada.
     * @return Objeto Usuario si las credenciales coinciden, o null si son incorrectas.
     */
    public Usuario login(String correo, String contrasenaPlana) {
        if (correo == null || contrasenaPlana == null) {
            return null;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String contrasenaHasheada = HashUtil.md5(contrasenaPlana);

        String[] projection = {"id_usuario", "nombre", "correo", "contrasena", "fecha_creacion"};
        String selection = "LOWER(correo) = ? AND contrasena = ?";
        String[] selectionArgs = {correo.trim().toLowerCase(), contrasenaHasheada};

        try (Cursor cursor = db.query("USUARIO", projection, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("correo"));
                String pass = cursor.getString(cursor.getColumnIndexOrThrow("contrasena"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha_creacion"));

                return new Usuario(idUsuario, nombre, email, pass, fecha);
            }
        }
        return null;
    }

    /**
     * Comprueba si un correo ya se encuentra registrado.
     */
    public boolean existeCorreo(String correo) {
        if (correo == null) return false;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "LOWER(correo) = ?";
        String[] selectionArgs = {correo.trim().toLowerCase()};

        try (Cursor cursor = db.query("USUARIO", new String[]{"id_usuario"}, selection, selectionArgs, null, null, null)) {
            return cursor != null && cursor.getCount() > 0;
        }
    }

    /**
     * Obtiene los datos de un usuario por su ID.
     */
    public Usuario obtenerPorId(int idUsuario) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"id_usuario", "nombre", "correo", "contrasena", "fecha_creacion"};
        String selection = "id_usuario = ?";
        String[] selectionArgs = {String.valueOf(idUsuario)};

        try (Cursor cursor = db.query("USUARIO", projection, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("correo"));
                String pass = cursor.getString(cursor.getColumnIndexOrThrow("contrasena"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha_creacion"));

                return new Usuario(idUsuario, nombre, email, pass, fecha);
            }
        }
        return null;
    }
}
