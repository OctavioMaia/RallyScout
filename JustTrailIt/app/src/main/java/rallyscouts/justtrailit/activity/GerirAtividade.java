package rallyscouts.justtrailit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import rallyscouts.justtrailit.R;
import rallyscouts.justtrailit.business.Batedor;
import rallyscouts.justtrailit.data.AtividadeDAO;
import rallyscouts.justtrailit.data.BatedorDAO;

public class GerirAtividade extends AppCompatActivity {


    private BatedorDAO batedores;
    private AtividadeDAO atividades;

    private rallyscouts.justtrailit.business.Atividade atividadeAProcess;
    private Batedor batedorLogin;

    private TextView atividade;
    private TextView equipa;
    private TextView veiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerir_atividade);

        this.setTitle("Gerir Atividade");

        batedores = new BatedorDAO(GerirAtividade.this);
        atividades = new AtividadeDAO(GerirAtividade.this);

        atividade = (TextView) findViewById(R.id.nAtividade);
        equipa = (TextView) findViewById(R.id.id_Equipa);

        batedorLogin = batedores.getBatedor((String)getIntent().getExtras().get("email"));
        atividadeAProcess = atividades.getAtividade(batedorLogin.getAtividade());

        atividade.setText("Atividade " + atividadeAProcess.getIdAtividade());
        equipa.setText("Equipa: " + atividadeAProcess.getNomeEquipa());

    }

    public void verVeiculos(View v){
        Intent verVeiculos = new Intent(GerirAtividade.this, ShowVeiculos.class);
        verVeiculos.putExtra("idAtividade",atividadeAProcess.getIdAtividade());
        GerirAtividade.this.startActivity(verVeiculos);
    }

    public void verPercurso(View v){
        Intent verpercurso = new Intent(GerirAtividade.this, MapaPercurso.class);
        verpercurso.putExtra("idAtividade",atividadeAProcess.getIdAtividade());
        GerirAtividade.this.startActivity(verpercurso);
    }

    public void registarNota(View v){

        Intent registarnota = new Intent(GerirAtividade.this, RegistarNota.class);
        registarnota.putExtra("idAtividade",batedorLogin.getAtividade());
        GerirAtividade.this.startActivity(registarnota);

    }


    public void verNotas(View v){

        Intent vernotas = new Intent(GerirAtividade.this, Notas.class);
        GerirAtividade.this.startActivity(vernotas);


    }

}
