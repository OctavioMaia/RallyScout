package rallyscouts.justtrailit.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import rallyscouts.justtrailit.R;
import rallyscouts.justtrailit.business.Nota;
import rallyscouts.justtrailit.data.NotaDAO;

public class RegistarNota extends AppCompatActivity {

    public static final String ID_ATIVIDADE = "idAtividade";
    public static final String LOC_LATITUDE = "latitude";
    public static final String LOC_LONGUITUDE = "longuitude";

    private NotaDAO notas;

    private int idAtividade;
    private Nota notaToSave;

    public static final int CAMARA_REQUEST = 10;

    private EditText mensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar_nota);

        this.notas = new NotaDAO(RegistarNota.this);

        Location loc = new Location("");
        loc.setLatitude(getIntent().getExtras().getDouble(LOC_LATITUDE));
        loc.setLongitude(getIntent().getExtras().getDouble(LOC_LONGUITUDE));

        this.idAtividade = getIntent().getExtras().getInt(ID_ATIVIDADE);
        this.notaToSave = new Nota(idAtividade,loc);

        this.setTitle("Registar Nota: " + idAtividade +
                "Lat: " + this.notaToSave.getLocalRegisto().getLatitude() +
                "Lng: " + this.notaToSave.getLocalRegisto().getLongitude()
        );

        this.mensagem = (EditText)findViewById(R.id.id_InserirTexto);

        mensagem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //tinha o focus e ficou sem ele
                if(!hasFocus){
                    notaToSave.setNotaTextual(mensagem.getText().toString());
                }else{
                    mensagem.setText(notaToSave.getNotaTextual());
                }
            }
        });
    }


    public void registarImagem(View v) {
        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camaraIntent,CAMARA_REQUEST);
    }


    public void confirmarSubmissao(View v) {
        //Falta definir a voz a latitude e a longitude.
        notas.insertNota(idAtividade,notaToSave);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            if(requestCode==CAMARA_REQUEST)
            {
                Bitmap camaraImage = (Bitmap) data.getExtras().get("data");
                notaToSave.addImagem(camaraImage);
            }
        }
    }
}
