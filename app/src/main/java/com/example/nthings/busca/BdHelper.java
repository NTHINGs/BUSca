package com.example.nthings.busca;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nthings on 30/06/14.
 * Esta clase ayuda a administrar el archivo de bases de datos para la aplicación
 */
public class BdHelper extends SQLiteOpenHelper {

    //Constantes de la base de datos
    private static final String DB_NAME = "busca.sqlite";
    private static final int DB_SCHEME_VERSION = 1;

    public BdHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        String[] creartablas = new String[]{DatabaseManager.CREAR_TABLA_RUTAS, DatabaseManager.CREAR_TABLA_COORDENADAS };
        for (int i = 0; i < creartablas.length; i++) {
            DB.execSQL(creartablas[i]);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
