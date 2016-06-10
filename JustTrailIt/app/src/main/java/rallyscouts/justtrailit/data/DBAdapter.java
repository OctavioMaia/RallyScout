package rallyscouts.justtrailit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.Map;

/**
 * Created by rjaf on 09/06/16.
 */
public class DBAdapter extends SQLiteOpenHelper {

    public static final String TAG = "DBAdapter";
    public static final String DATABASE_NAME = "JustTrailIt.db";
    public static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_ATIVIDADE =
            "CREATE TABLE " + AtividadeDAO.ATIVIDADE_TABLE_NAME + " ( " +
                    AtividadeDAO.ATIVIDADE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    AtividadeDAO.ATIVIDADE_COLUMN_EQUIPA_EMAIL + " VARCHAR(50) NOT NULL, " +
                    AtividadeDAO.ATIVIDADE_COLUMN_EQUIPA_NOME + " VARCHAR(50) NOT NULL )";

    private static final String CREATE_TABLE_BATEDOR =
            "CREATE TABLE " + BatedorDAO.BATEDOR_TABLE_NAME + " ( " +
                    BatedorDAO.BATEDOR_COLUMN_EMAIL + " VARCHAR(50) PRIMARY KEY, " +
                    BatedorDAO.BATEDOR_COLUMN_PASSWORD + " VARCHAR(50) NOT NULL, " +
                    BatedorDAO.BATEDOR_COLUMN_NOME + " VARCHAR(50) NOT NULL, " +
                    BatedorDAO.BATEDOR_COLUMN_ATIVIDADE + "INTEGER, " +
                    "FOREIGN KEY ( " + BatedorDAO.BATEDOR_COLUMN_ATIVIDADE + " ) REFERENCES " + AtividadeDAO.ATIVIDADE_TABLE_NAME + "( " + AtividadeDAO.ATIVIDADE_COLUMN_ID + " ))";

    private static final String CREATE_TABLE_MAPA =
            "CREATE TABLE " + MapaDAO.MAPA_TABLE_NAME + " ( " +
                    MapaDAO.MAPA_COLUMN_ATIVIDADE_ID + " INTEGER PRIMARY KEY, " +
                    MapaDAO.MAPA_COLUMN_NOME_PROVA + " VARCHAR(50) NOT NULL," +
                    "FOREIGN KEY ( " + MapaDAO.MAPA_COLUMN_ATIVIDADE_ID + ") REFERENCES " + AtividadeDAO.ATIVIDADE_TABLE_NAME + "( " + AtividadeDAO.ATIVIDADE_COLUMN_ID + "))";


    private static final String CREATE_TABLE_MAPA_COORDENADAS =
            "CREATE TABLE " + MapaDAO.MAPA_COORDENADAS_TABLE_NAME + " ( " +
                    MapaDAO.MAPA_COORDENADAS_COLUMN_MAPA + " INTEGER, " +
                    MapaDAO.MAPA_COORDENADAS_COLUMN_NR_COORDENADA + " INTEGER, " +
                    MapaDAO.MAPA_COORDENADAS_COLUMN_LATITUDE + " FLOAT NOT NULL, " +
                    MapaDAO.MAPA_COORDENADAS_COLUMN_LONGITUDE + " FLOAT NOT NULL, " +
                    "PRIMARY KEY ( "+ MapaDAO.MAPA_COORDENADAS_COLUMN_MAPA + " , " + MapaDAO.MAPA_COORDENADAS_COLUMN_NR_COORDENADA + " ), " +
                    "FOREIGN KEY ( " + MapaDAO.MAPA_COLUMN_ATIVIDADE_ID + ") REFERENCES " + AtividadeDAO.ATIVIDADE_TABLE_NAME + " ( " + AtividadeDAO.ATIVIDADE_COLUMN_ID + "))";

    private static final String CREATE_TABLE_NOTA =
            "CREATE TABLE " + NotaDAO.NOTA_TABLE_NAME + " ( " +
                    NotaDAO.NOTA_COLUMN_ID_NOTA + " INTEGER, " +
                    NotaDAO.NOTA_COLUMN_ATIVIDADE + " INTEGER, " +
                    NotaDAO.NOTA_COLUMN_NOTA_TEXTUAL + " TEXT," +
                    NotaDAO.NOTA_COLUMN_AUDIO + " BLOB, " +
                    NotaDAO.NOTA_COLUMN_LATITUDE + " FLOAT NOT NULL, " +
                    NotaDAO.NOTA_COLUMN_LONGITUDE + " FLOAT NOT NULL, " +
                    "PRIMARY KEY ( " + NotaDAO.NOTA_COLUMN_ID_NOTA + " , " + NotaDAO.NOTA_COLUMN_ATIVIDADE + " ), " +
                    "FOREIGN KEY " + NotaDAO.NOTA_COLUMN_ATIVIDADE + " REFERENCES " + AtividadeDAO.ATIVIDADE_TABLE_NAME + " ( " + AtividadeDAO.ATIVIDADE_COLUMN_ID + "))";

    private static final String CREATE_TABLE_IMAGEM =
            "CREATE TABLE " + NotaDAO.IMAGEM_TABLE_NAME + " ( " +
                    NotaDAO.IMAGEM_COLUMN_IMAGE + " BLOB, " +
                    NotaDAO.IMAGEM_COLUMN_NOTA + " INTEGER, " +
                    NotaDAO.IMAGEM_COLUMN_ATIVIDADE + " INTEGER, " +
                    "PRIMARY KEY ( " + NotaDAO.IMAGEM_COLUMN_IMAGE + " , " + NotaDAO.IMAGEM_COLUMN_NOTA + " , " + NotaDAO.IMAGEM_COLUMN_ATIVIDADE + " )" +
                    "FOREIGN KEY ( " + NotaDAO.IMAGEM_COLUMN_NOTA + ") REFERENCES " + NotaDAO.NOTA_TABLE_NAME + " ( " + NotaDAO.NOTA_COLUMN_ID_NOTA + "), " +
                    "FOREIGN KEY ( " + NotaDAO.IMAGEM_COLUMN_ATIVIDADE + ") REFERENCES " + AtividadeDAO.ATIVIDADE_TABLE_NAME + " ( " + AtividadeDAO.ATIVIDADE_COLUMN_ID + "))";

    private static final String CREATE_TABLE_VEICULO =
            " CREATE TABLE " + VeiculoDAO.VEICULO_TABLE_NAME + " ( " +
                    VeiculoDAO.VEICULO_COLUMN_CHASSI + " VARCHAR(50) PRIMARY KEY, " +
                    VeiculoDAO.VEICULO_COLUMN_MARCA + " VARCHAR(50), " +
                    VeiculoDAO.VEICULO_COLUMN_MODELO + " VARCHAR(50), " +
                    VeiculoDAO.VEICULO_COLUMN_ATIVIDADE + " INTEGER NOT NULL, " +
                    "FOREIGN KEY ( " + VeiculoDAO.VEICULO_COLUMN_CHASSI + " ) REFERENCES " + AtividadeDAO.ATIVIDADE_TABLE_NAME + "( " + AtividadeDAO.ATIVIDADE_COLUMN_ID + " ))";

    private static final String CREATE_TABLE_VEICULO_CARACTERISTICAS =
            " CREATE TABLE " + VeiculoDAO.VEICULO_CARACTERISTICAS_TABLE_NAME + " ( " +
                    VeiculoDAO.VEICULO_CARACTERISTICAS_COLUMN_CARACTERISTICA + " VARCHAR(400), " +
                    VeiculoDAO.VEICULO_CARACTERISTICAS_COLUMN_CHASSI + " VARCHAR(50), " +
                    "PRIMARY KEY ( " + VeiculoDAO.VEICULO_CARACTERISTICAS_COLUMN_CHASSI + " , " + VeiculoDAO.VEICULO_CARACTERISTICAS_COLUMN_CARACTERISTICA + " ), " +
                    "FOREIGN KEY ( " + VeiculoDAO.VEICULO_CARACTERISTICAS_COLUMN_CHASSI + " ) REFERENCES " + VeiculoDAO.VEICULO_TABLE_NAME + "( " + VeiculoDAO.VEICULO_COLUMN_CHASSI + " ))";


    public DBAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_ATIVIDADE);
        }catch (android.database.SQLException eSQL){
            Log.e(TAG, "Error creating table " + AtividadeDAO.ATIVIDADE_TABLE_NAME);
        }
        try {
            db.execSQL(CREATE_TABLE_BATEDOR);
        }catch (android.database.SQLException eSQL){
            Log.e(TAG, "Error creating table " + BatedorDAO.BATEDOR_TABLE_NAME);
        }
        try {
            db.execSQL(CREATE_TABLE_MAPA);
        }catch (android.database.SQLException eSQL){
            Log.e(TAG, "Error creating the table " + MapaDAO.MAPA_TABLE_NAME);
        }
        try {
            db.execSQL(CREATE_TABLE_MAPA_COORDENADAS);
        }catch (android.database.SQLException eSQL){
            Log.e(TAG, "Error creating the table " + MapaDAO.MAPA_COORDENADAS_TABLE_NAME);
        }
        try {
            db.execSQL(CREATE_TABLE_NOTA);
        }catch (android.database.SQLException eSQL){
            Log.e(TAG, "Error creating the table " + NotaDAO.NOTA_TABLE_NAME);
        }
        try {
            db.execSQL(CREATE_TABLE_IMAGEM);
        }catch (android.database.SQLException eSQL){
            Log.e(TAG, "Error creating the table " + NotaDAO.IMAGEM_TABLE_NAME);
        }
        try {
            db.execSQL(CREATE_TABLE_VEICULO);
        }catch (android.database.SQLException eSQL){
            Log.e(TAG, "Error creating the table " + VeiculoDAO.VEICULO_CARACTERISTICAS_TABLE_NAME);
        }
        try {
            db.execSQL(CREATE_TABLE_VEICULO_CARACTERISTICAS);
        }catch (android.database.SQLException eSQL){
            Log.e(TAG, "Error creating the table " + VeiculoDAO.VEICULO_TABLE_NAME);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading the database from version " + oldVersion + " to "+ newVersion);
        // clear all data
        db.execSQL("DROP TABLE IF EXISTS " + AtividadeDAO.ATIVIDADE_TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + BatedorDAO.BATEDOR_TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + MapaDAO.MAPA_TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + MapaDAO.MAPA_COORDENADAS_TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + NotaDAO.NOTA_TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + NotaDAO.IMAGEM_TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + VeiculoDAO.VEICULO_TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + VeiculoDAO.VEICULO_CARACTERISTICAS_TABLE_NAME );
        // recreate the tables
        onCreate(db);
    }
}
