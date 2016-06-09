package rallyscouts.justtrailit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import rallyscouts.justtrailit.business.Veiculo;

/**
 * Created by rjaf on 09/06/16.
 */
public class VeiculoDAO extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "JustTrailIt.db";
    public static final String VEICULO_TABLE_NAME = "Veiculo";
    public static final String VEICULO_COLUMN_CHASSI = "Chassi";
    public static final String VEICULO_COLUMN_MARCA = "Marca";
    public static final String VEICULO_COLUMN_MODELO = "Modelo";
    public static final String VEICULO_COLUMN_ATIVIDADE = "Atividade";

    public static final String VEICULO_CARACTERISTICAS_TABLE_NAME = "VeiculoCaracteristicas";
    public static final String VEICULO_CARACTERISTICAS_COLUMN_CARACTERISTICA = "Caracteristicas";
    public static final String VEICULO_CARACTERISTICAS_COLUMN_CHASSI = "Chassi";

    public VeiculoDAO(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + VEICULO_TABLE_NAME + " ( " +
                        VEICULO_COLUMN_CHASSI + " varchar(50) primary key, " +
                        VEICULO_COLUMN_MARCA + " varchar(50), " +
                        VEICULO_COLUMN_MODELO + " varchar(50), " +
                        VEICULO_COLUMN_ATIVIDADE + " integer, " +
                        " foreign key( " + VEICULO_COLUMN_CHASSI + " ) references " + AtividadeDAO.ATIVIDADE_TABLE_NAME + "( " + AtividadeDAO.ATIVIDADE_COLUMN_ID + " ))"
        );

        db.execSQL(" CREATE TABLE " + VEICULO_CARACTERISTICAS_TABLE_NAME + " ( " +
                        VEICULO_CARACTERISTICAS_COLUMN_CARACTERISTICA + " varchar(400), " +
                        VEICULO_CARACTERISTICAS_COLUMN_CHASSI + " varchar(50), " +
                        " primary key ( " + VEICULO_CARACTERISTICAS_COLUMN_CHASSI + " , " + VEICULO_CARACTERISTICAS_COLUMN_CARACTERISTICA + " ), " +
                        " foreign key( " + VEICULO_CARACTERISTICAS_COLUMN_CHASSI + " ) references " + VEICULO_TABLE_NAME + "( " + VEICULO_COLUMN_CHASSI + " ))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertVeiculo  (String chassi, String marca, String modelo, int atividade)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(VEICULO_COLUMN_CHASSI, chassi);
        contentValues.put(VEICULO_COLUMN_MARCA, marca);
        contentValues.put(VEICULO_COLUMN_MODELO, modelo);
        contentValues.put(VEICULO_COLUMN_ATIVIDADE, atividade);
        if( db.insert(VEICULO_TABLE_NAME, null, contentValues) == -1 ) return false;
        return true;
    }

    public boolean insertVeiculoCaracteristica  (String chassi, String caracteritica) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(VEICULO_CARACTERISTICAS_COLUMN_CHASSI, chassi);
        contentValues.put(VEICULO_CARACTERISTICAS_COLUMN_CARACTERISTICA, caracteritica);
        if( db.insert(VEICULO_CARACTERISTICAS_TABLE_NAME, null, contentValues) == -1 ) return false;
        return true;
    }

    public int numberOfVeiculos(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, VEICULO_TABLE_NAME);
        return numRows;
    }

    /**
     *  Method getAllVeiculos que retira da base de dados todos os veiculos nela presente
     * @return
     */
    public ArrayList<Veiculo> getAllVeiculos() {
        ArrayList<Veiculo> veiculos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor resVeiculos =  db.rawQuery("select * from " + VEICULO_TABLE_NAME, null);
        resVeiculos.moveToFirst();

        while(resVeiculos.isAfterLast() == false){
            ArrayList<String> caracteristicas = new ArrayList<>();
            String chassi = resVeiculos.getString(resVeiculos.getColumnIndex(VEICULO_COLUMN_CHASSI));

            String[] args = { chassi };

            Cursor resCaracteristicas =  db.rawQuery( "select " + VEICULO_CARACTERISTICAS_COLUMN_CARACTERISTICA + " from " + VEICULO_CARACTERISTICAS_TABLE_NAME +
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
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor resVeiculos =  db.rawQuery("select * from " + VEICULO_TABLE_NAME + " where " + VEICULO_COLUMN_ATIVIDADE + " = ? ", new String[]{ ""+atividade } );
        resVeiculos.moveToFirst();

        while(resVeiculos.isAfterLast() == false){
            ArrayList<String> caracteristicas = new ArrayList<>();
            String chassi = resVeiculos.getString(resVeiculos.getColumnIndex(VEICULO_COLUMN_CHASSI));


            Cursor resCaracteristicas =  db.rawQuery( "select " + VEICULO_CARACTERISTICAS_COLUMN_CARACTERISTICA + " from " + VEICULO_CARACTERISTICAS_TABLE_NAME +
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
