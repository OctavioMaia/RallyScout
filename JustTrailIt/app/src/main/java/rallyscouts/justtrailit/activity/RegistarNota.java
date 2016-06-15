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
import rallyscouts.justtrailit.data.NotaDAO;

public class RegistarNota extends AppCompatActivity {

    public static final int CAMARA_REQUEST = 10;
    private NotaDAO notas;
    //Resposavel por guardar o texto do utilizador;
    String guardarTexto = new String();
    //Resposavel por guardar as imagens do utilizador;
    ArrayList<Bitmap> imagens = new ArrayList<Bitmap>() ;
    //Resposavel por guardar a voz do utilizador;
    private byte[] voz;
    Location l;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar_nota);
        this.setTitle("Registar Nota");

        LocationManager myManager;
        LocationListener myListener;
        myManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        myListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(location!=null)
                {
                    l.setLatitude(location.getLatitude());
                    l.setLongitude(location.getLongitude());
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        myManager.requestLocationUpdates("gps",0,0,myListener);

        this.notas = new NotaDAO(RegistarNota.this);
        TextView nNota;
        nNota = (TextView) findViewById(R.id.id_RegistoNota);
        nNota.setText("RegistoNota " + (int)getIntent().getExtras().get("idNota"));;

        final EditText mensagem = (EditText)findViewById(R.id.id_InserirTexto);
        Button botaoInserir = (Button)findViewById(R.id.button_RegistarTexto);

        if (botaoInserir != null)
        {
            botaoInserir.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(mensagem != null)
                    {
                        if(!mensagem.getText().toString().equals(""))
                        {
                            Toast.makeText(getApplicationContext(), "Texto guardado com sucesso", Toast.LENGTH_SHORT).show();
                            guardarTexto += mensagem.getText().toString();
                        }
                    }
                }
            });
        }
    }


    public void registarImagem(View v)
    {
    Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(camaraIntent,CAMARA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            if(requestCode==CAMARA_REQUEST)
            {
                Bitmap camaraImage = (Bitmap) data.getExtras().get("data");
                imagens.add(camaraImage);
            }
        }
    }

    public void confirmarSubmissao(View v)
    {
        //Falta definir a voz a latitude e a longitude.
        //notas.insertNota((int)getIntent().getExtras().get("idNota"),(int)getIntent().getExtras().get("idAtividade",guardarTexto,voz,latitude,longitude,imagens);
        Intent submissao = new Intent(RegistarNota.this, GerirAtividade.class);
        RegistarNota.this.startActivity(submissao);
    }
}
