package rallyscouts.justtrailit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import rallyscouts.justtrailit.business.Atividade;

/**
 * Created by rjaf on 09/06/16.
 */
public class AtividadeDAO extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "JustTrailIt.db";
    public static final String ATIVIDADE_TABLE_NAME = "Atividade";
    public static final String ATIVIDADE_COLUMN_ID = "id_Atividade";
    public static final String ATIVIDADE_COLUMN_EQUIPA_EMAIL = "Equipa_Email";
    public static final String ATIVIDADE_COLUMN_EQUIPA_NOME = "Equipa_Nome";


    public AtividadeDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ATIVIDADE_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertAtividade(int idAtividade, String equipaEmail, String equipaNome) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ATIVIDADE_COLUMN_ID, idAtividade);
        contentValues.put(ATIVIDADE_COLUMN_EQUIPA_EMAIL, equipaEmail);
        contentValues.put(ATIVIDADE_COLUMN_EQUIPA_NOME, equipaNome);
        if( db.insert(ATIVIDADE_TABLE_NAME, null, contentValues) == -1) return false ;
        return true;
    }

    public Atividade getData(int idAtividade){
        Atividade resAtiv = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " +  ATIVIDADE_TABLE_NAME  + " WHERE " + ATIVIDADE_COLUMN_ID + " = ? " , new String[]{ ""+idAtividade } );

        if(res.getCount()>0){
            resAtiv = new Atividade();
        }

        return resAtiv;
    }
}
