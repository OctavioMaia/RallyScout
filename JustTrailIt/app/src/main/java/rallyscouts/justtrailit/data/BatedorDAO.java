package rallyscouts.justtrailit.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rjaf on 09/06/16.
 */
public class BatedorDAO extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "JustTrailIt.db";
    public static final String BATEDOR_TABLE_NAME = "Veiculo";
    public static final String BATEDOR_COLUMN_EMAIL = "Chassi";
    public static final String BATEDOR_COLUMN_MARCA = "Marca";
    public static final String BATEDOR_COLUMN_MODELO = "Modelo";
    public static final String VEICULO_COLUMN_ATIVIDADE = "Atividade";
    public static final BATEDOR_

    public BatedorDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
