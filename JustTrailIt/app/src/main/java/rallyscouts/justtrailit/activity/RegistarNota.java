package rallyscouts.justtrailit.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import rallyscouts.justtrailit.R;
import rallyscouts.justtrailit.data.NotaDAO;

public class RegistarNota extends AppCompatActivity {

    public static final int CAMARA_REQUEST = 10;
    private NotaDAO notas;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar_nota);
        this.notas = new NotaDAO(RegistarNota.this);
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
                notas.insertImagem(notas.getMaiorNota()+1, (int)getIntent().getExtras().get("idAtividade"), camaraImage);

            }
        }
    }
}
