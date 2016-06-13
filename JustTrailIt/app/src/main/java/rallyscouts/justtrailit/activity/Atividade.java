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

        TextView atividade = findViewById(R.id.nAtividade);
        atividade.setText("Atividade " +  );

        TextView equipa = findViewById(R.id.id_Equipa);
        equipa.setText("Equipa: " + );

        TextView veiculo = findViewById(R.id.id_Veiculo);
        veiculo.setText("Veiculo: " + );

        registarNota();
        verNotas();
    }

    private void registarNota()
    {
        Button registarnota = findViewById(R.id.button_RegistarNota);
        registarnota.setOnClickListener( new View.OnClickListener())
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Atividade.this, RegistarNota.class));
            }
        }

    }

    private void verNotas()
    {
        Button vernota = findViewById(R.id.button_VerNotas);
        vernota.setOnClickListener( new View.OnClickListener())
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Atividade.this, Notas.class));
            }
        }

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return false;
    }


*/
}
