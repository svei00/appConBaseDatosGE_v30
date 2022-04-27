package com.example.appconbasedatosge_v30;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper; // Importamos esta clase para poder utilizar el SQL Lite

import androidx.annotation.Nullable;

/*
Código de Referencia
https://www.youtube.com/watch?v=TxkdWX3UaNk&list=PLyvsggKtwbLX06iMtXnRGX5lyjiiMaT2y&index=31
 */

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper { // Exportamos los atributos y métodos de la clase SQLiteOpenHelper e implementamos métodos con ALT + Enter así como el Constructor
    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db1) { // Sustituimos SQLiteDatabase por el nombre de la base de datos, en este caso db1
        // Creamos la tabla donde se guardarán los articulos
        db1.execSQL("create table articulos (codigo int primary key unique, descripcion text, precio real)"); // execSQL es el comando que ejecuta comandos SQL y pondremos la sentencia SQL donde artículos es el nombre de la tabla

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
