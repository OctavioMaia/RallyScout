package rallyscouts.justtrailit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;

import rallyscouts.justtrailit.business.Atividade;

/**
 * Created by rjaf on 09/06/16.
 */
public class AtividadeDAO {

    public static final String TAG = "AtividadeDAO";

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private DBAdapter myDBadapter;

    public static final String ATIVIDADE_TABLE_NAME = "Atividade";
    public static final String ATIVIDADE_COLUMN_ID = "id_Atividade";
    public static final String ATIVIDADE_COLUMN_EQUIPA_EMAIL = "Equipa_Email";
    public static final String ATIVIDADE_COLUMN_EQUIPA_NOME = "Equipa_Nome";


    public AtividadeDAO(Context mContext) {
        this.mContext = mContext;
        this.myDBadapter = new DBAdapter( mContext );
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

    /**
     * insertAtividade metodo que insere uma atividade na base de dados
     * @param idAtividade
     * @param equipaEmail
     * @param equipaNome
     * @return
     */
    public boolean insertAtividade(int idAtividade, String equipaEmail, String equipaNome) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ATIVIDADE_COLUMN_ID, idAtividade);
        contentValues.put(ATIVIDADE_COLUMN_EQUIPA_EMAIL, equipaEmail);
        contentValues.put(ATIVIDADE_COLUMN_EQUIPA_NOME, equipaNome);
        if( mDatabase.insert(ATIVIDADE_TABLE_NAME, null, contentValues) == -1) return false ;
        return true;
    }

    /**
     * getAtividade metodo que procura por uma atividade pelo seu id
     * @param idAtividade
     * @return
     */
    public Atividade getAtividade(int idAtividade){
        Atividade resAtiv = null;
        Cursor res =  mDatabase.rawQuery( "SELECT * FROM " +  ATIVIDADE_TABLE_NAME  + " WHERE " + ATIVIDADE_COLUMN_ID + " = ? " , new String[]{ ""+idAtividade } );
        if(res.getCount()>0){
            resAtiv = new Atividade(
                    res.getInt(res.getColumnIndex(ATIVIDADE_COLUMN_ID)),
                    res.getString(res.getColumnIndex(ATIVIDADE_COLUMN_EQUIPA_NOME)),
                    res.getString(res.getColumnIndex(ATIVIDADE_COLUMN_EQUIPA_EMAIL))
            );
        }
        return resAtiv;
    }
}
