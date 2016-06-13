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

    public boolean insertVeiculo  (String chassi, String marca, String modelo, int atividade) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(VEICULO_COLUMN_CHASSI, chassi);
        contentValues.put(VEICULO_COLUMN_MARCA, marca);
        contentValues.put(VEICULO_COLUMN_MODELO, modelo);
        contentValues.put(VEICULO_COLUMN_ATIVIDADE, atividade);
        if( mDatabase.insert(VEICULO_TABLE_NAME, null, contentValues) == -1 ) return false;
        return true;
    }

    public boolean insertVeiculoCaracteristica  (String chassi, String caracteritica) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(VEICULO_CARACTERISTICAS_COLUMN_CHASSI, chassi);
        contentValues.put(VEICULO_CARACTERISTICAS_COLUMN_CARACTERISTICA, caracteritica);
        if( mDatabase.insert(VEICULO_CARACTERISTICAS_TABLE_NAME, null, contentValues) == -1 ) return false;
        return true;
    }



    public int deleteVeiculo(String chassi){
        deleteAllCaracteristicasVeiculo(chassi);
        return mDatabase.delete(VEICULO_TABLE_NAME,VEICULO_COLUMN_CHASSI + " = ?",new String[]{ chassi });
    }

    public int deleteAllVeiculoAtividade(int idAtividade){
        Cursor resVeiculos =  mDatabase.rawQuery("SELECT * FROM " + VEICULO_TABLE_NAME + " WHERE " + VEICULO_COLUMN_ATIVIDADE, null);

    }

    public int deleteAllCaracteristicasVeiculo(String chassi){
        return mDatabase.delete(VEICULO_CARACTERISTICAS_TABLE_NAME,VEICULO_CARACTERISTICAS_COLUMN_CHASSI + " = ?", new String[]{ chassi });
    }




    /**
     *  Method getAllVeiculos que retira da base de dados todos os veiculos nela presente
     * @return
     */
    public ArrayList<Veiculo> getAllVeiculos() {
        ArrayList<Veiculo> veiculos = new ArrayList<>();
        Cursor resVeiculos =  mDatabase.rawQuery("select * from " + VEICULO_TABLE_NAME, null);
        resVeiculos.moveToFirst();

        while(resVeiculos.isAfterLast() == false){
            ArrayList<String> caracteristicas = new ArrayList<>();
            String chassi = resVeiculos.getString(resVeiculos.getColumnIndex(VEICULO_COLUMN_CHASSI));

            String[] args = { chassi };

            Cursor resCaracteristicas =  mDatabase.rawQuery( "select " + VEICULO_CARACTERISTICAS_COLUMN_CARACTERISTICA + " from " + VEICULO_CARACTERISTICAS_TABLE_NAME +
                    " where " + VEICULO_CARACTERISTICAS_COLUMN_CHASSI + " = ? ", args  );

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


    /**
     * Method getAllVeiculos que retira da base de dados todos os veiculos referentes a uma atividade
     * @param atividade
     * @return
     */
    public ArrayList<Veiculo> getAllVeiculos(int atividade) {
        ArrayList<Veiculo> veiculos = new ArrayList<>();

        Cursor resVeiculos =  mDatabase.rawQuery("select * from " + VEICULO_TABLE_NAME + " where " + VEICULO_COLUMN_ATIVIDADE + " = ? ", new String[]{ ""+atividade } );
        resVeiculos.moveToFirst();

        while(resVeiculos.isAfterLast() == false){
            ArrayList<String> caracteristicas = new ArrayList<>();
            String chassi = resVeiculos.getString(resVeiculos.getColumnIndex(VEICULO_COLUMN_CHASSI));


            Cursor resCaracteristicas =  mDatabase.rawQuery( "select " + VEICULO_CARACTERISTICAS_COLUMN_CARACTERISTICA + " from " + VEICULO_CARACTERISTICAS_TABLE_NAME +
                    " where " + VEICULO_CARACTERISTICAS_COLUMN_CHASSI + " = ? ", new String[]{ chassi }  );

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
