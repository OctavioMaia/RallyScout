package rallyscouts.justtrailit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

import rallyscouts.justtrailit.business.Batedor;

/**
 * Created by rjaf on 09/06/16.
 */
public class BatedorDAO {

    public static final String TAG = "BatedorDAO";

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private DBAdapter myDBadapter;

    public static final String BATEDOR_TABLE_NAME = "Batedor";
    public static final String BATEDOR_COLUMN_EMAIL = "Email";
    public static final String BATEDOR_COLUMN_PASSWORD = "Password";
    public static final String BATEDOR_COLUMN_NOME = "Nome";
    public static final String BATEDOR_COLUMN_ATIVIDADE = "Atividade";

    public BatedorDAO(Context mContext) {
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


    /**
     * method para inserir um batedor
     * @param email
     * @param pass
     * @param nome
     * @return
     */
    public boolean insertBatedor(String email, String pass, String nome) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BATEDOR_COLUMN_EMAIL, email);
        contentValues.put(BATEDOR_COLUMN_PASSWORD, pass);
        contentValues.put(BATEDOR_COLUMN_NOME, nome);
        if( mDatabase.insert(BATEDOR_TABLE_NAME, null, contentValues) == -1 ) return false;
        return true;
    }

    /**
     * method para atualizar um batedor
     * @param email
     * @param pass
     * @param nome
     * @param atividade
     * @return
     */
    public boolean updateBatedor (String email, String pass, String nome, int atividade) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BATEDOR_COLUMN_EMAIL, email);
        contentValues.put(BATEDOR_COLUMN_PASSWORD, pass);
        contentValues.put(BATEDOR_COLUMN_NOME, nome);
        contentValues.put(BATEDOR_COLUMN_ATIVIDADE, atividade);
        if( mDatabase.update(BATEDOR_TABLE_NAME, contentValues, BATEDOR_COLUMN_EMAIL + " = ? ", new String[]{email}) == 0) return false;
        return true;
    }

    /**
     * getBatedor metodo devolve a infromação presente na base de dados sobre um batedor atravez do seu email
     * @param email
     * @return
     */
    public Batedor getBatedor(String email){
        Batedor bat = null;
        Cursor res = mDatabase.rawQuery("select * from " + BATEDOR_TABLE_NAME + " where " + BATEDOR_COLUMN_EMAIL + " = ?" , new String[]{ email });
        if(res.getCount()>0){
            bat = new Batedor(
                    res.getString(res.getColumnIndex(BATEDOR_COLUMN_EMAIL)),
                    res.getString(res.getColumnIndex(BATEDOR_COLUMN_NOME)),
                    res.getString(res.getColumnIndex(BATEDOR_COLUMN_PASSWORD)),
                    res.getInt(res.getColumnIndex(BATEDOR_COLUMN_ATIVIDADE))
            );
        }
        return bat;
    }

    /**
     * removeBatedor metodo que remove um batedor da base de dados
     * @param email
     * @return
     */
    public int removeBatedor(String email){
        return mDatabase.delete(BATEDOR_TABLE_NAME, BATEDOR_COLUMN_EMAIL + " = ?" , new String[]{ email });
    }
}
