package rallyscouts.justtrailit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rjaf on 09/06/16.
 */
public class MapaDAO extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "JustTrailIt.db";
    public static final String VEICULO_TABLE_NAME = "Veiculo";
    public static final String VEICULO_COLUMN_CHASSI = "Chassi";
    public static final String VEICULO_COLUMN_MARCA = "Marca";
    public static final String VEICULO_COLUMN_MODELO = "Modelo";
    public static final String VEICULO_COLUMN_ATIVIDADE = "Atividade";

    public MapaDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
