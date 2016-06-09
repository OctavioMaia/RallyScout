package rallyscouts.justtrailit.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rjaf on 09/06/16.
 */
public class AtividadeDAO extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "JustTrailIt.db";
    public static final String ATIVIDADE_TABLE_NAME = "Atividade";
    public static final String ATIVIDADE_COLUMN_ID = "id_Atividade";
    public static final String ATIVIDADE_COLUMN_EQUIPA_EMAIL = "Equipa_Email";
    public static final String ATIVIDADE_COLUMN_EQUIPA_NOME = "Equipa_Nome";
    public static final String ATIVIDADE_COLUMN_MAPA = "Mapa";

    public AtividadeDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, 1);
    }

    public AtividadeDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
