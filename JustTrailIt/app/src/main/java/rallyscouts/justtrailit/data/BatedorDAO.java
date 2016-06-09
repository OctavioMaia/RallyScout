package rallyscouts.justtrailit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rjaf on 09/06/16.
 */
public class BatedorDAO extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "JustTrailIt.db";
    public static final String BATEDOR_TABLE_NAME = "Batedor";
    public static final String BATEDOR_COLUMN_EMAIL = "Email";
    public static final String BATEDOR_COLUMN_PASSWORD = "Password";
    public static final String BATEDOR_COLUMN_NOME = "Nome";
    public static final String BATEDOR_COLUMN_ATIVIDADE = "Atividade";

    public BatedorDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + BATEDOR_TABLE_NAME + " ( " +
                        BATEDOR_COLUMN_EMAIL + " varchar(50) primary key, " +
                        BATEDOR_COLUMN_PASSWORD + " varchar(50), " +
                        BATEDOR_COLUMN_NOME + " varchar(50), " +
                        BATEDOR_COLUMN_ATIVIDADE + "integer, " +
                        " foreign key( " + BATEDOR_COLUMN_ATIVIDADE + " ) references " + AtividadeDAO.ATIVIDADE_TABLE_NAME + "( " + AtividadeDAO.ATIVIDADE_COLUMN_ID + " ))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BATEDOR_TABLE_NAME);
        onCreate(db);
    }

    /**
     * method para inserir um batedor
     * @param email
     * @param pass
     * @param nome
     * @return
     */
    public boolean insertBatedor(String email, String pass, String nome) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BATEDOR_COLUMN_EMAIL, email);
        contentValues.put(BATEDOR_COLUMN_PASSWORD, pass);
        contentValues.put(BATEDOR_COLUMN_NOME, nome);
        if( db.insert(BATEDOR_TABLE_NAME, null, contentValues) == -1 ) return false;
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
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BATEDOR_COLUMN_EMAIL, email);
        contentValues.put(BATEDOR_COLUMN_PASSWORD, pass);
        contentValues.put(BATEDOR_COLUMN_NOME, nome);
        contentValues.put(BATEDOR_COLUMN_ATIVIDADE, atividade);
        db.update(BATEDOR_TABLE_NAME, contentValues, BATEDOR_COLUMN_EMAIL + " = ? ", new String[]{email});
        return true;
    }

    /**
     * getBatedor metodo devolve a infromação presente na base de dados sobre um batedor atravez do seu email
     * @param email
     * @return
     */
    public Cursor getBatedor(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + BATEDOR_TABLE_NAME + " where " + BATEDOR_COLUMN_EMAIL + " = ?" , new String[]{ email });
        return res;
    }

    /**
     * removeBatedor metodo que remove um batedor da base de dados
     * @param email
     * @return
     */
    public int removeBatedor(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(BATEDOR_TABLE_NAME, BATEDOR_COLUMN_EMAIL + " = ?" , new String[]{ email });
    }
}
