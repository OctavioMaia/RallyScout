package rallyscouts.justtrailit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
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

    public static final String NOTA_TABLE_NAME = "Nota";
    public static final String NOTA_COLUMN_ID_NOTA = "id_Nota";
    public static final String NOTA_COLUMN_ATIVIDADE = "idAtividade";
    public static final String NOTA_COLUMN_NOTA_TEXTUAL = "NotaTextual";
    public static final String NOTA_COLUMN_AUDIO = "Audio";
    public static final String NOTA_COLUMN_LATITUDE = "Latitude";
    public static final String NOTA_COLUMN_LONGITUDE = "Longitude";

    public static final String IMAGEM_TABLE_NAME = "Imagem";
    public static final String IMAGEM_COLUMN_ID = "idImagem";
    public static final String IMAGEM_COLUMN_IMAGE = "Imagem";
    public static final String IMAGEM_COLUMN_NOTA = "Nota";
    public static final String IMAGEM_COLUMN_ATIVIDADE = "idAtividade";

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

    public boolean insertNota(int idAtividade, Nota nota){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTA_COLUMN_ID_NOTA, nota.getIdNota());
        Log.i(TAG,"NOTA:"+nota.getIdNota());

        contentValues.put(NOTA_COLUMN_ATIVIDADE, idAtividade);
        Log.i(TAG,""+idAtividade);

        contentValues.put(NOTA_COLUMN_NOTA_TEXTUAL, nota.getNotaTextual());
        Log.i(TAG,nota.getNotaTextual());
        contentValues.put(NOTA_COLUMN_AUDIO, nota.getVoice());
        if(nota.getVoice()!=null){
            Log.i(TAG,""+nota.getVoice().length);
        }

        contentValues.put(NOTA_COLUMN_LATITUDE,nota.getLocalRegisto().getLatitude());
        Log.i(TAG,""+nota.getLocalRegisto().getLatitude());

        contentValues.put(NOTA_COLUMN_LONGITUDE,nota.getLocalRegisto().getLongitude());
        Log.i(TAG,""+nota.getLocalRegisto().getLongitude());

        if( mDatabase.insert(NOTA_TABLE_NAME, null, contentValues) == -1) return false ;
        for ( Bitmap im : nota.getImagens() ) {
            insertImagem(nota.getIdNota(),idAtividade,im);
            Log.i(TAG,"IMAGE:"+im.getByteCount());
        }
        return true;
    }

    public boolean insertImagem(int idNota, int idAtividade, Bitmap imagem){
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGEM_COLUMN_NOTA, idNota);
        contentValues.put(IMAGEM_COLUMN_ATIVIDADE, idAtividade);
        //int size = imagem.getRowBytes() * imagem.getHeight();
        //ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        //imagem.copyPixelsToBuffer(byteBuffer);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();

        contentValues.put(IMAGEM_COLUMN_IMAGE, bArray);
        if( mDatabase.insert(IMAGEM_TABLE_NAME, null, contentValues) == -1) return false ;
        return true;
    }

    public int getLargerID(int idAtividade){
        int large = -1;
        Cursor nota = mDatabase.rawQuery(
                "SELECT " + NOTA_COLUMN_ID_NOTA +
                        " FROM " + NOTA_TABLE_NAME +
                        " WHERE " + NOTA_COLUMN_ATIVIDADE + " = ?" , new String[]{ ""+idAtividade });
        if(nota.getCount()==0) {
            large = -1;
        } else{
            nota.moveToFirst();
            while (nota.isAfterLast()==false){
                if(nota.getInt(nota.getColumnIndex(NOTA_COLUMN_ID_NOTA)) > large) { large = nota.getInt(nota.getColumnIndex(NOTA_COLUMN_ID_NOTA));}
                nota.moveToNext();
            }
            nota.close();
        }
        return large;

    }

    public int getMaiorNota(int idAtividade) {
        Cursor nota = mDatabase.rawQuery(
                "SELECT NVL(MAX( " + NOTA_COLUMN_ID_NOTA +" ),0)" +
                        "FROM " + NOTA_TABLE_NAME +
                        " WHERE " + NOTA_COLUMN_ATIVIDADE + " = ?" , new String[]{ ""+idAtividade });
        int maior =  nota.getInt(0);
        nota.close();
        return maior;
    }


    public Nota getNota(int idNota, int idAtividade){
        Nota not = null;
        Cursor resNota = mDatabase.rawQuery(
                "SELECT * FROM " + NOTA_TABLE_NAME +
                        " WHERE " + NOTA_COLUMN_ID_NOTA + " = ? AND " + NOTA_COLUMN_ATIVIDADE + " = ?",
                new String[]{ ""+idNota,""+idAtividade }
        );

        resNota.moveToFirst();
        if(resNota.getCount()>0){
            Location loc = new Location("");
            loc.setLatitude(resNota.getDouble(resNota.getColumnIndex(NOTA_COLUMN_LATITUDE)));
            loc.setLongitude(resNota.getDouble(resNota.getColumnIndex(NOTA_COLUMN_LONGITUDE)));
            not = new Nota(
                    idNota,
                    getAllImagens(idNota,idAtividade),
                    loc,
                    resNota.getString(resNota.getColumnIndex(NOTA_COLUMN_NOTA_TEXTUAL)),
                    resNota.getBlob(resNota.getColumnIndex(NOTA_COLUMN_AUDIO)));
        }
        resNota.close();
        return not;
    }


    public List<Bitmap> getAllImagens(int idNota, int idAtividade){
        ArrayList<Bitmap> imagens = new ArrayList<>();

        Cursor resImagens = mDatabase.rawQuery(
                "SELECT * FROM " + IMAGEM_TABLE_NAME +
                        " WHERE " + IMAGEM_COLUMN_NOTA + " = ? AND " + IMAGEM_COLUMN_ATIVIDADE + " = ?",
                new String[]{ ""+idNota, ""+idAtividade }
        );

        resImagens.moveToFirst();
        while(resImagens.isAfterLast()==false){
            byte[] byteArray = resImagens.getBlob(resImagens.getColumnIndex(IMAGEM_COLUMN_IMAGE));
            Log.i(TAG,"Numero de bytes iamgem: " + byteArray.length);

            Bitmap bm = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

            if (bm==null){
                Log.i(TAG,"O bitmap é null");
            }
            imagens.add(bm);
            resImagens.moveToNext();
        }
        resImagens.close();
        return imagens;
    }

    public List<Nota> getAllNotas(int idAtividade){
        List<Nota> notas = new ArrayList<>();

        Cursor res = mDatabase.rawQuery("SELECT " + NOTA_COLUMN_ID_NOTA +
                " FROM " + NOTA_TABLE_NAME +
                " WHERE " + NOTA_COLUMN_ATIVIDADE + " = ?",
                new String[]{""+idAtividade});

        res.moveToFirst();
        while(res.isAfterLast()==false){
            Nota n = getNota(res.getInt(res.getColumnIndex(NOTA_COLUMN_ID_NOTA)),idAtividade);
            Log.i(TAG,n.toString());
            if(n!=null){
                notas.add(n);
            }
            res.moveToNext();
        }
        res.close();
        return notas;

    }




    private boolean insertNota(int idNota, int idAtividade, String notaTextual, byte[] audio, float lat, float lng, List<Bitmap> imagens){
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

    public int deleteAllImageAtividade(int idAtividade){
        return mDatabase.delete(IMAGEM_TABLE_NAME,IMAGEM_COLUMN_ATIVIDADE + " = ?", new String[]{""+idAtividade});
    }

    public int deleteAllNotaAtividade(int idAtividade) {
        deleteAllImageAtividade(idAtividade);
        return mDatabase.delete(NOTA_TABLE_NAME, NOTA_COLUMN_ATIVIDADE + " = ?", new String[]{ ""+idAtividade });
    }
}
