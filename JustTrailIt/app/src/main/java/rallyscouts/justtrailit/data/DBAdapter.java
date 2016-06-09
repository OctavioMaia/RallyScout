package rallyscouts.justtrailit.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rjaf on 09/06/16.
 */
public class DBAdapter extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "JustTrailIt";
    public static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_ATIVIDADE =
            "CREATE TABLE " + AtividadeDAO.ATIVIDADE_TABLE_NAME + " ( " +
                    AtividadeDAO.ATIVIDADE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    AtividadeDAO.ATIVIDADE_COLUMN_EQUIPA_EMAIL + " VARCHAR(50) PRIMARY KEY, " +
                    AtividadeDAO.ATIVIDADE_COLUMN_EQUIPA_NOME + " VARCHAR(50) )";

    private static final String CREATE_TABLE_BATEDOR =
            "CREATE TABLE " + BatedorDAO.BATEDOR_TABLE_NAME + " ( " +
                    BatedorDAO.BATEDOR_COLUMN_EMAIL + " varchar(50) primary key, " +
                    BatedorDAO.BATEDOR_COLUMN_PASSWORD + " varchar(50), " +
                    BatedorDAO.BATEDOR_COLUMN_NOME + " varchar(50), " +
                    BatedorDAO.BATEDOR_COLUMN_ATIVIDADE + "integer, " +
                    " foreign key( " + BatedorDAO.BATEDOR_COLUMN_ATIVIDADE + " ) references " + AtividadeDAO.ATIVIDADE_TABLE_NAME + "( " + AtividadeDAO.ATIVIDADE_COLUMN_ID + " ))";

    private static final String CREATE_TABLE_MAPA = ;

    private static final String CREATE_TABLE_NOTA =
            "CREATE TABLE " + ;

    private static final String CREATE_TABLE_VEICULO =
            " CREATE TABLE " + VeiculoDAO.VEICULO_TABLE_NAME + " ( " +
                    VeiculoDAO.VEICULO_COLUMN_CHASSI + " VARCHAR(50) PRIMARY KEY, " +
                    VeiculoDAO.VEICULO_COLUMN_MARCA + " varchar(50), " +
                    VeiculoDAO.VEICULO_COLUMN_MODELO + " varchar(50), " +
                    VeiculoDAO.VEICULO_COLUMN_ATIVIDADE + " integer, " +
                    " foreign key( " + VeiculoDAO.VEICULO_COLUMN_CHASSI + " ) references " + AtividadeDAO.ATIVIDADE_TABLE_NAME + "( " + AtividadeDAO.ATIVIDADE_COLUMN_ID + " ))"
            ;

    private static final String CREATE_TABLE_VEICULO_CARACTERISTICAS =
            " CREATE TABLE " + VeiculoDAO.VEICULO_CARACTERISTICAS_TABLE_NAME + " ( " +
                    VeiculoDAO.VEICULO_CARACTERISTICAS_COLUMN_CARACTERISTICA + " varchar(400), " +
                    VeiculoDAO.VEICULO_CARACTERISTICAS_COLUMN_CHASSI + " varchar(50), " +
                    " primary key ( " + VeiculoDAO.VEICULO_CARACTERISTICAS_COLUMN_CHASSI + " , " + VeiculoDAO.VEICULO_CARACTERISTICAS_COLUMN_CARACTERISTICA + " ), " +
                    " foreign key( " + VeiculoDAO.VEICULO_CARACTERISTICAS_COLUMN_CHASSI + " ) references " + VeiculoDAO.VEICULO_TABLE_NAME + "( " + VeiculoDAO.VEICULO_COLUMN_CHASSI + " ))";


    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;



    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
