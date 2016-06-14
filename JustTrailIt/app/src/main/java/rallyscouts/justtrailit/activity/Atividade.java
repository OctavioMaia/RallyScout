package rallyscouts.justtrailit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import rallyscouts.justtrailit.R;

public class Atividade extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade);

        TextView atividade = (TextView) findViewById(R.id.nAtividade);
        atividade.setText("Atividade " + "4" );

        TextView equipa = (TextView) findViewById(R.id.id_Equipa);
        equipa.setText("Equipa: " + "1");

        TextView veiculo = (TextView) findViewById(R.id.id_Veiculo);
        veiculo.setText("Veiculo: " + "ola");

    }

    public void registarNota(View v){

        Intent registarnota = new Intent(Atividade.this, RegistarNota.class);
        Atividade.this.startActivity(registarnota);
    }


    public void verNotas(View v){

        Intent vernotas = new Intent(Atividade.this, Notas.class);
        Atividade.this.startActivity(vernotas);
    }

}
