package rallyscouts.justtrailit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rallyscouts.justtrailit.business.Veiculo;

/**
 * Created by rjaf on 09/06/16.
 */
public class VeiculoDAO {


    private Context mContext;
    private SQLiteDatabase mDatabase;
    private DBAdapter myDBadapter;

    public static final String TAG = "VeiculoDAO";

    public static final String VEICULO_TABLE_NAME = "Veiculo";
    public static final String VEICULO_COLUMN_CHASSI = "Chassi";
    public static final String VEICULO_COLUMN_MARCA = "Marca";
    public static final String VEICULO_COLUMN_MODELO = "Modelo";
    public static final String VEICULO_COLUMN_ATIVIDADE = "Atividade";

    public static final String VEICULO_CARACTERISTICAS_TABLE_NAME = "VeiculoCaracteristicas";
    public static final String VEICULO_CARACTERISTICAS_COLUMN_CARACTERISTICA = "Caracteristicas";
    public static final String VEICULO_CARACTERISTICAS_COLUMN_CHASSI = "Chassi";


    public VeiculoDAO(Context mContext) {
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
     * metodo que insere um veiculo
     * @param vec
     * @return
     */
    public boolean insertVeiculo(int idAtividade, Veiculo vec) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(VEICULO_COLUMN_CHASSI, vec.getChassi());
        contentValues.put(VEICULO_COLUMN_MARCA, vec.getMarca());
        contentValues.put(VEICULO_COLUMN_MODELO, vec.getModelo());
        contentValues.put(VEICULO_COLUMN_ATIVIDADE, idAtividade);
        if( mDatabase.insert(VEICULO_TABLE_NAME, null, contentValues) == -1 ) return false;
        return true;
    }


    public void insertVeiculos(int idAtividade, List<Veiculo> lista){
        for (Veiculo v :lista ) {
            insertVeiculo(idAtividade,v);
        }
    }

    /**
     * metodo que insere uma caracteristica para um determinado veiculo
     * @param chassi
     * @param caracteritica
     * @return
     */
    public boolean insertVeiculoCaracteristica  (String chassi, String caracteritica) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(VEICULO_CARACTERISTICAS_COLUMN_CHASSI, chassi);
        contentValues.put(VEICULO_CARACTERISTICAS_COLUMN_CARACTERISTICA, caracteritica);
        if( mDatabase.insert(VEICULO_CARACTERISTICAS_TABLE_NAME, null, contentValues) == -1 ) return false;
        return true;
    }

    /**
     * metodo que remove um veiculo com um determinado chassi
     * @param chassi
     * @return
     */
    public int deleteVeiculo(String chassi){
        deleteAllCaracteristicasVeiculo(chassi);
        return mDatabase.delete(VEICULO_TABLE_NAME,VEICULO_COLUMN_CHASSI + " = ?",new String[]{ chassi });
    }


    /**
     * metodo que remove todos os veiculos de uma atividade
     * @param idAtividade
     */
    public void deleteAllVeiculoAtividade(int idAtividade){
        Cursor resVeiculos =  mDatabase.rawQuery("SELECT * FROM " + VEICULO_TABLE_NAME + " WHERE " + VEICULO_COLUMN_ATIVIDADE + " = ?", new String[]{ ""+idAtividade });
        resVeiculos.moveToFirst();
        while(resVeiculos.isAfterLast()){
            deleteVeiculo(resVeiculos.getString(resVeiculos.getColumnIndex(VEICULO_COLUMN_CHASSI)));
            resVeiculos.moveToNext();
        }
    }

    /**
     * metodo que remove todas as caracateristicas de um veiculo
     * @param chassi
     * @return
     */
    public int deleteAllCaracteristicasVeiculo(String chassi){
        return mDatabase.delete(VEICULO_CARACTERISTICAS_TABLE_NAME,VEICULO_CARACTERISTICAS_COLUMN_CHASSI + " = ?", new String[]{ chassi });
    }

    /**
     *  Method getAllVeiculos que retira da base de dados todos os veiculos nela presente
     * @return
     */
    public ArrayList<Veiculo> getAllVeiculos() {
        ArrayList<Veiculo> veiculos = new ArrayList<>();
        Cursor resVeiculos =  mDatabase.rawQuery("SELECT * FROM " + VEICULO_TABLE_NAME, null);
        resVeiculos.moveToFirst();

        while(resVeiculos.isAfterLast() == false){
            ArrayList<String> caracteristicas = new ArrayList<>();
            String chassi = resVeiculos.getString(resVeiculos.getColumnIndex(VEICULO_COLUMN_CHASSI));

            Cursor resCaracteristicas =  mDatabase.rawQuery( "SELECT " + VEICULO_CARACTERISTICAS_COLUMN_CARACTERISTICA + " FROM " + VEICULO_CARACTERISTICAS_TABLE_NAME +
                    " WHERE " + VEICULO_CARACTERISTICAS_COLUMN_CHASSI + " = ? ", new String[] { chassi } );

            resCaracteristicas.moveToFirst();
            while (resCaracteristicas.isAfterLast() == false){
                caracteristicas.add(resCaracteristicas.getString(resCaracteristicas.getColumnIndex(VEICULO_CARACTERISTICAS_COLUMN_CARACTERISTICA)));
                resCaracteristicas.moveToNext();
            }

            Veiculo v = new Veiculo(
                    resVeiculos.getString(resVeiculos.getColumnIndex(VEICULO_COLUMN_CHASSI)),
                    resVeiculos.getString(resVeiculos.getColumnIndex(VEICULO_COLUMN_MARCA)),
                    resVeiculos.getString(resVeiculos.getColumnIndex(VEICULO_COLUMN_MODELO)),
                    caracteristicas
            );

            veiculos.add(v);
            resVeiculos.moveToNext();
        }
        return veiculos;
    }

}
