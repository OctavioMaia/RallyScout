package rallyscouts.justtrailit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.util.Log;

import java.sql.SQLException;
import java.util.List;

import rallyscouts.justtrailit.business.Atividade;

/**
 * Created by rjaf on 09/06/16.
 */
public class NotaDAO {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private DBAdapter myDBadapter;

    public static final String TAG = "NotaDAO";

    public static final String NOTA_TABLE_NAME = "Mapa";
    public static final String NOTA_COLUMN_ID_NOTA = "id_Nota";
    public static final String NOTA_COLUMN_ATIVIDADE = "Atividade";
    public static final String NOTA_COLUMN_NOTA_TEXTUAL = "NotaTextual";
    public static final String NOTA_COLUMN_AUDIO = "Audio";
    public static final String NOTA_COLUMN_LATITUDE = "Latitude";
    public static final String NOTA_COLUMN_LONGITUDE = "Longitude";

    public static final String IMAGEM_TABLE_NAME = "Imagem";
    public static final String IMAGEM_COLUMN_IMAGE = "Mapa";
    public static final String IMAGEM_COLUMN_NOTA = "Nota";
    public static final String IMAGEM_COLUMN_ATIVIDADE = "Atividade";

    public NotaDAO(Context mContext) {
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

    public boolean insertNota(int idNota, int idAtividade, String notaTextual, byte[] audio, float lat, float lng, List<byte[]> imagens){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTA_COLUMN_ID_NOTA, idNota);
        contentValues.put(NOTA_COLUMN_ATIVIDADE, idAtividade);
        contentValues.put(NOTA_COLUMN_NOTA_TEXTUAL, notaTextual);
        contentValues.put(NOTA_COLUMN_AUDIO, audio);
        contentValues.put(NOTA_COLUMN_LATITUDE,lat);
        contentValues.put(NOTA_COLUMN_LONGITUDE,lng);
        if( mDatabase.insert(NOTA_TABLE_NAME, null, contentValues) == -1) return false ;

        return true;
    }

    public boolean insertImagem(int idNota, int idAtividade, ){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTA_COLUMN_ID_NOTA, idNota);
        contentValues.put(NOTA_COLUMN_ATIVIDADE, idAtividade);
        contentValues.put(NOTA_COLUMN_NOTA_TEXTUAL, );
        contentValues.put(NOTA_COLUMN_AUDIO, audio);
        contentValues.put(NOTA_COLUMN_LATITUDE,lat);
        contentValues.put(NOTA_COLUMN_LONGITUDE,lng);
        if( mDatabase.insert(NOTA_TABLE_NAME, null, contentValues) == -1) return false ;
        return true;
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
                    Integer.parseInt(res.getString(res.getColumnIndex(ATIVIDADE_COLUMN_ID))),
                    res.getString(res.getColumnIndex(ATIVIDADE_COLUMN_EQUIPA_NOME)),
                    res.getString(res.getColumnIndex(ATIVIDADE_COLUMN_EQUIPA_EMAIL))
            );
        }
        return resAtiv;
    }
}
