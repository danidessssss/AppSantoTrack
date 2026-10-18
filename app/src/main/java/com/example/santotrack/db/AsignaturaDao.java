package com.example.santotrack.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.santotrack.model.Asignatura;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la tabla ASIGNATURA en SQLite.
 * Gestiona el almacenamiento y consulta de las asignaturas asociadas a cada usuario.
 */
public class AsignaturaDao {

    private final DatabaseHelper dbHelper;

    public AsignaturaDao(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Inserta una nueva asignatura vinculada al usuario y al color seleccionados.
     *
     * @param idUsuario ID del usuario que crea la asignatura.
     * @param idColor ID del color seleccionado de la paleta.
     * @param nombre Nombre de la asignatura.
     * @param notaAprobacion Nota mínima de aprobación (ej: 4.0).
     * @return id_asignatura generado, o -1 en caso de error.
     */
    public long insertar(int idUsuario, int idColor, String nombre, double notaAprobacion) {
        if (nombre == null || nombre.trim().isEmpty() || idUsuario <= 0) {
            return -1;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_usuario", idUsuario);
        values.put("id_color", idColor);
        values.put("nombre", nombre.trim());
        values.put("nota_aprobacion", notaAprobacion);

        return db.insert("ASIGNATURA", null, values);
    }

    /**
     * Obtiene la lista de asignaturas pertenecientes al usuario especificado,
     * uniendo con la tabla COLOR para obtener el código hexadecimal de presentación.
     *
     * @param idUsuario ID del usuario autenticado.
     * @return Lista de asignaturas del usuario.
     */
    public List<Asignatura> obtenerPorUsuario(int idUsuario) {
        List<Asignatura> lista = new ArrayList<>();
        if (idUsuario <= 0) {
            return lista;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT a.id_asignatura, a.id_usuario, a.id_color, a.nombre, " +
                "a.nota_aprobacion, a.fecha_creacion, c.codigo_hex, c.nombre AS color_nombre " +
                "FROM ASIGNATURA a " +
                "LEFT JOIN COLOR c ON a.id_color = c.id_color " +
                "WHERE a.id_usuario = ? " +
                "ORDER BY a.id_asignatura DESC";

        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idUsuario)})) {
            while (cursor != null && cursor.moveToNext()) {
                int idAsignatura = cursor.getInt(cursor.getColumnIndexOrThrow("id_asignatura"));
                int idUser = cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario"));
                int idColor = cursor.getInt(cursor.getColumnIndexOrThrow("id_color"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                double nota = cursor.getDouble(cursor.getColumnIndexOrThrow("nota_aprobacion"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha_creacion"));
                String colorHex = cursor.getString(cursor.getColumnIndexOrThrow("codigo_hex"));
                String colorNombre = cursor.getString(cursor.getColumnIndexOrThrow("color_nombre"));

                Asignatura asignatura = new Asignatura(idAsignatura, idUser, idColor, nombre, nota, fecha);
                asignatura.setColorHex(colorHex != null ? colorHex : "#3525CD");
                asignatura.setColorNombre(colorNombre);
                lista.add(asignatura);
            }
        }
        return lista;
    }

    /**
     * Elimina una asignatura garantizando que solo el usuario propietario pueda borrarla.
     *
     * @param idAsignatura ID de la asignatura a eliminar.
     * @param idUsuario ID del usuario autenticado (control de acceso).
     * @return true si se eliminó correctamente, false en caso contrario.
     */
    public boolean eliminar(int idAsignatura, int idUsuario) {
        if (idAsignatura <= 0 || idUsuario <= 0) {
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int filasAfectadas = db.delete("ASIGNATURA", "id_asignatura = ? AND id_usuario = ?",
                new String[]{String.valueOf(idAsignatura), String.valueOf(idUsuario)});

        return filasAfectadas > 0;
    }

    /**
     * Cuenta la cantidad de asignaturas inscritas por el usuario.
     */
    public int contarPorUsuario(int idUsuario) {
        if (idUsuario <= 0) return 0;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM ASIGNATURA WHERE id_usuario = ?";
        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idUsuario)})) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        }
        return 0;
    }
}
