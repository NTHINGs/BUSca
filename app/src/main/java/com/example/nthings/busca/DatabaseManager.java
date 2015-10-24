package com.example.nthings.busca;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by nthings on 23/10/15.
 */
public class DatabaseManager {
    public static final String TABLE_NAME_RUTAS="rutas";
    public static final String TABLE_NAME_COORDENADAS="coordenadas";

    public static final String id_="id_";
    public static final String nombreruta="nombreruta";

    public static final String latitud="latitud";
    public static final String longitud="longitud";
    public static final String ruta="ruta";

    public static final String CREAR_TABLA_RUTAS="create table "+TABLE_NAME_RUTAS+" ("
            +id_+" integer primary key autoincrement,"
            +nombreruta+" text not null);";

    public static final String CREAR_TABLA_COORDENADAS="create table "+TABLE_NAME_COORDENADAS+" ("
            +id_+" integer primary key autoincrement,"
            +latitud+" real not null,"
            +longitud+" real not null,"
            +ruta+" integer not null);";
    private BdHelper helper;
    private SQLiteDatabase db;
    Context context;

    public DatabaseManager(Context contexto) {
        //Crear base de datos y obtenerla en modo escritura
        this.context=contexto;
        helper = new BdHelper(contexto);
        db = helper.getWritableDatabase();
    }

    public ContentValues generarContentValues(double latitud, double longitud, int ruta){
        ContentValues contentValues=new ContentValues();
        contentValues.put(this.latitud,latitud);
        contentValues.put(this.longitud,longitud);
        contentValues.put(this.ruta,ruta);
        return contentValues;
    }
    public void insertarRuta(double latitud, double longitud, int ruta){
        db.insert(TABLE_NAME_COORDENADAS, null, generarContentValues(latitud, longitud, ruta));
    }

    public PolylineOptions sacarRuta(int ruta){
        PolylineOptions linea=new PolylineOptions().width(4).color(context.getResources().getColor(R.color.orrange));
        Cursor cursor=db.rawQuery("SELECT latitud, longitud FROM coordenadas WHERE ruta=?",new String[]{String.valueOf(ruta)});
        if(cursor.moveToFirst()){
            do{
                linea.add(new LatLng(cursor.getDouble(0),cursor.getDouble(1)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return linea;
    }
    public void borrar(){
        db.delete(TABLE_NAME_COORDENADAS, ruta + "=?", new String[]{"1"});
    }
}
