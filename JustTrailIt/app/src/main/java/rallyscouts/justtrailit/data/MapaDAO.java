package rallyscouts.justtrailit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import junit.runner.Version;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import rallyscouts.justtrailit.business.Mapa;

/**
 * Created by rjaf on 09/06/16.
 */
public class MapaDAO {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private DBAdapter myDBadapter;

    public static final String TAG = "MapaDAO";

    public static final String MAPA_TABLE_NAME = "Mapa";
    public static final String MAPA_COLUMN_ATIVIDADE_ID = "Atividade";
    public static final String MAPA_COLUMN_NOME_PROVA = "NomeProva";

    public static final String MAPA_COORDENADAS_TABLE_NAME = "CoordenadasMapa";
    public static final String MAPA_COORDENADAS_COLUMN_MAPA = "Mapa";
    public static final String MAPA_COORDENADAS_COLUMN_NR_COORDENADA = "NrCoordenada";
    public static final String MAPA_COORDENADAS_COLUMN_LONGITUDE = "Longitude";
    public static final String MAPA_COORDENADAS_COLUMN_LATITUDE = "Latitude";


    public MapaDAO(Context mContext) {
        this.mContext = mContext;
        this.mContext = mContext;
        this.myDBadapter = new DBAdapter( this.mContext );
        // open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = myDBadapter.getWritableDatabase();
    }

    public void close() {
        myDBadapter.close();
    }

    public boolean insertMapa(Mapa p){
        boolean ret = true;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MAPA_COLUMN_ATIVIDADE_ID, p.getIdMapa());
        contentValues.put(MAPA_COLUMN_NOME_PROVA, p.getNomeProva());
        if( mDatabase.insert(MAPA_TABLE_NAME, null, contentValues) == -1 ) ret = false;

        Map<Integer,Location> listaCoords = p.getCoord();

        for (Integer ordem : listaCoords.keySet()) {
            insertMapaCoordenada(p.getIdMapa(), ordem, listaCoords.get(ordem));
        }
        return ret;
    }

    public boolean insertMapa(int idMapa, String nomeProva) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MAPA_COLUMN_ATIVIDADE_ID, idMapa);
        contentValues.put(MAPA_COLUMN_NOME_PROVA, nomeProva);
        if( mDatabase.insert(MAPA_TABLE_NAME, null, contentValues) == -1 ) return false;
        return true;
    }

    public boolean insertMapaCoordenada(int idMapa, Integer ordem, Location loc){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MAPA_COORDENADAS_COLUMN_MAPA, idMapa );
        contentValues.put(MAPA_COORDENADAS_COLUMN_NR_COORDENADA, ordem );
        contentValues.put(MAPA_COORDENADAS_COLUMN_LONGITUDE, loc.getLongitude() );
        contentValues.put(MAPA_COORDENADAS_COLUMN_LATITUDE, loc.getLatitude());
        if( mDatabase.insert(MAPA_COORDENADAS_TABLE_NAME, null, contentValues) == -1 ) return false;
        return true;
    }

    public boolean insertMapaCoordenada(int idMapa, int nrOrdem, float lng, float lat) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MAPA_COORDENADAS_COLUMN_MAPA, idMapa );
        contentValues.put(MAPA_COORDENADAS_COLUMN_NR_COORDENADA, nrOrdem );
        contentValues.put(MAPA_COORDENADAS_COLUMN_LONGITUDE, lng );
        contentValues.put(MAPA_COORDENADAS_COLUMN_LATITUDE, lat);
        if( mDatabase.insert(MAPA_COORDENADAS_TABLE_NAME, null, contentValues) == -1 ) return false;
        return true;
    }

    public Mapa getMapa(int idMapa){
        Mapa map = null;
        Cursor res = mDatabase.rawQuery("SELECT * FROM " + MAPA_TABLE_NAME + " WHERE " + MAPA_COLUMN_ATIVIDADE_ID + " = ?" , new String[]{ ""+idMapa });
        res.moveToFirst();
        if(res.getCount()>0){
            map = new Mapa(getCoordenadas(idMapa), res.getInt(res.getColumnIndex(MAPA_COLUMN_ATIVIDADE_ID)), res.getString(res.getColumnIndex(MAPA_COLUMN_NOME_PROVA)));


        }
        return map;
    }

    public Map<Integer,Location> getCoordenadas(int idMapa){
        Map<Integer,Location> coord = new HashMap<>();
        Cursor res = mDatabase.rawQuery("SELECT * FROM " + MAPA_COORDENADAS_TABLE_NAME + " WHERE " + MAPA_COORDENADAS_COLUMN_MAPA + " = ?" , new String[]{ ""+idMapa } );
        res.moveToFirst();
        if(res.getCount()>0){
            while(res.isAfterLast() == false){
                Location loc = new Location("");
                loc.setLatitude(res.getFloat(res.getColumnIndex(MAPA_COORDENADAS_COLUMN_LATITUDE)));
                loc.setLongitude(res.getFloat(res.getColumnIndex(MAPA_COORDENADAS_COLUMN_LONGITUDE)));
                coord.put(res.getInt(res.getColumnIndex(MAPA_COORDENADAS_COLUMN_NR_COORDENADA)), loc);
                res.moveToNext();
            }
            res.close();
        }
        return coord;
    }

    public int deleteMapaCoordenadas(int idMapa){
        return mDatabase.delete(MAPA_COORDENADAS_TABLE_NAME,MAPA_COORDENADAS_COLUMN_MAPA + " = ?", new String[]{ ""+idMapa });
    }

    public int deleteMapa(int idMapa){
        deleteMapaCoordenadas(idMapa);
        return mDatabase.delete(MAPA_TABLE_NAME,MAPA_COLUMN_ATIVIDADE_ID + " = ?", new String[]{ ""+idMapa });
    }

}
