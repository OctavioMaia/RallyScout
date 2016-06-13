package rallyscouts.justtrailit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rallyscouts.justtrailit.business.Nota;

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

    public boolean insertNota(int idNota, int idAtividade, String notaTextual, byte[] audio, float lat, float lng, List<Bitmap> imagens){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTA_COLUMN_ID_NOTA, idNota);
        contentValues.put(NOTA_COLUMN_ATIVIDADE, idAtividade);
        contentValues.put(NOTA_COLUMN_NOTA_TEXTUAL, notaTextual);
        contentValues.put(NOTA_COLUMN_AUDIO, audio);
        contentValues.put(NOTA_COLUMN_LATITUDE,lat);
        contentValues.put(NOTA_COLUMN_LONGITUDE,lng);
        if( mDatabase.insert(NOTA_TABLE_NAME, null, contentValues) == -1) return false ;
        for ( Bitmap im : imagens ) {
            insertImagem(idNota,idAtividade,im);
        }
        return true;
    }

    public boolean insertImagem(int idNota, int idAtividade, Bitmap imagem){
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGEM_COLUMN_NOTA, idNota);
        contentValues.put(IMAGEM_COLUMN_ATIVIDADE, idAtividade);
        contentValues.put(IMAGEM_COLUMN_IMAGE, (imagem));
        if( mDatabase.insert(IMAGEM_TABLE_NAME, null, contentValues) == -1) return false ;
        return true;
    }

    public Nota getNota(int idNota, int idAtividade){
        Nota not = null;
        Cursor resNota = mDatabase.rawQuery(
                "SELECT * FROM " + NOTA_TABLE_NAME +
                        " WHERE " + NOTA_COLUMN_ID_NOTA + " = ? AND " + NOTA_COLUMN_ATIVIDADE + " = ?",
                new String[]{ ""+idNota,""+idAtividade }
        );

        Cursor resImagens = mDatabase.rawQuery(
                "SELECT * FROM " + IMAGEM_TABLE_NAME +
                        " WHERE " + IMAGEM_COLUMN_NOTA + " = ? AND " + IMAGEM_COLUMN_ATIVIDADE + " = ?",
                new String[]{ ""+idNota,""+idAtividade }
        );

        List<Image> imagens = new ArrayList<>();
        return not;
    }


    public List<byte[]> getAllImagens(int idNota, int idAtividade){

    }

}
