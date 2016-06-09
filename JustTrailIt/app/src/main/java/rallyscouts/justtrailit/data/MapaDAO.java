package rallyscouts.justtrailit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rjaf on 09/06/16.
 */
public class MapaDAO extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "JustTrailIt.db";
    public static final String MAPA_TABLE_NAME = "Mapa";
    public static final String MAPA_COLUMN_ATIVIDADE = "Atividade";
    public static final String MAPA_COLUMN_ = "Marca";
    public static final String MAPA_COLUMN_MODELO = "Modelo";
    public static final String VEICULO_COLUMN_ATIVIDADE = "Atividade";

    public MapaDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
