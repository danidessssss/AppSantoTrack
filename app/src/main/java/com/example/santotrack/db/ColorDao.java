package com.example.santotrack.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.santotrack.model.ColorRamo;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la tabla COLOR en SQLite.
 */
public class ColorDao {

    private final DatabaseHelper dbHelper;

    public ColorDao(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    public List<ColorRamo> obtenerTodos() {
        List<ColorRamo> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT id_color, nombre, codigo_hex FROM COLOR ORDER BY id_color ASC";
        try (Cursor cursor = db.rawQuery(query, null)) {
            while (cursor != null && cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id_color"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String hex = cursor.getString(cursor.getColumnIndexOrThrow("codigo_hex"));
                lista.add(new ColorRamo(id, nombre, hex));
            }
        }
        return lista;
    }

    public ColorRamo obtenerPorId(int idColor) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT id_color, nombre, codigo_hex FROM COLOR WHERE id_color = ?";
        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idColor)})) {
            if (cursor != null && cursor.moveToFirst()) {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String hex = cursor.getString(cursor.getColumnIndexOrThrow("codigo_hex"));
                return new ColorRamo(idColor, nombre, hex);
            }
        }
        return null;
    }
}
