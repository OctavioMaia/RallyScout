package rallyscouts.justtrailit.activity;

import android.content.Intent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import rallyscouts.justtrailit.R;

public class MenuBatedor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_batedor);

        TextView atividadeDisponivel = (TextView) findViewById(R.id.textView_Atividade);
        atividadeDisponivel.setText((String)getIntent().getExtras().get("bat"));

    }


    public void gerirAtividade(View v){
        Intent intentGerirAtividade = new Intent(MenuBatedor.this, Atividade.class);
        MenuBatedor.this.startActivity(intentGerirAtividade);
    }

    public void downloadAtividade(View v){

    }

    public void uploadAtividade(View v){

    }







}
