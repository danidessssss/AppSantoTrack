package com.example.santotrack.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite OpenHelper para SantoTrack.
 * Implementa el esquema relacional según el modelo lógico:
 * USUARIO, COLOR, ASIGNATURA y EVALUACION.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "santotrack.db";
    public static final int DATABASE_VERSION = 1;

    // Singleton instance
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Tabla USUARIO
        db.execSQL("CREATE TABLE USUARIO (" +
                "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre VARCHAR(100) NOT NULL, " +
                "correo VARCHAR(150) NOT NULL UNIQUE, " +
                "contrasena VARCHAR(32) NOT NULL, " +
                "fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");");

        // 2. Tabla COLOR
        db.execSQL("CREATE TABLE COLOR (" +
                "id_color INTEGER PRIMARY KEY, " +
                "nombre VARCHAR(30) NOT NULL UNIQUE, " +
                "codigo_hex CHAR(7) NOT NULL UNIQUE" +
                ");");

        // 3. Tabla ASIGNATURA
        db.execSQL("CREATE TABLE ASIGNATURA (" +
                "id_asignatura INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_usuario INTEGER NOT NULL, " +
                "id_color INTEGER NOT NULL, " +
                "nombre VARCHAR(100) NOT NULL, " +
                "nota_aprobacion DECIMAL(2, 1) DEFAULT 4.0, " +
                "fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE, " +
                "FOREIGN KEY (id_color) REFERENCES COLOR(id_color)" +
                ");");

        // 4. Tabla EVALUACION
        db.execSQL("CREATE TABLE EVALUACION (" +
                "id_evaluacion INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_asignatura INTEGER NOT NULL, " +
                "nombre VARCHAR(100) NOT NULL, " +
                "porcentaje DECIMAL(5, 2) NOT NULL, " +
                "nota DECIMAL(2, 1), " +
                "fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (id_asignatura) REFERENCES ASIGNATURA(id_asignatura) ON DELETE CASCADE" +
                ");");

        // Poblar colores predefinidos del diseño de SantoTrack
        seedColors(db);
    }

    private void seedColors(SQLiteDatabase db) {
        db.execSQL("INSERT INTO COLOR (id_color, nombre, codigo_hex) VALUES (1, 'Azul Primario', '#3525CD');");
        db.execSQL("INSERT INTO COLOR (id_color, nombre, codigo_hex) VALUES (2, 'Índigo', '#4F46E5');");
        db.execSQL("INSERT INTO COLOR (id_color, nombre, codigo_hex) VALUES (3, 'Verde Esmeralda', '#005338');");
        db.execSQL("INSERT INTO COLOR (id_color, nombre, codigo_hex) VALUES (4, 'Cian Santo', '#00687A');");
        db.execSQL("INSERT INTO COLOR (id_color, nombre, codigo_hex) VALUES (5, 'Rojo Carmesí', '#BA1A1A');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS EVALUACION;");
        db.execSQL("DROP TABLE IF EXISTS ASIGNATURA;");
        db.execSQL("DROP TABLE IF EXISTS COLOR;");
        db.execSQL("DROP TABLE IF EXISTS USUARIO;");
        onCreate(db);
    }
}
