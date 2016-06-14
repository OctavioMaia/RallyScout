package rallyscouts.justtrailit.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import rallyscouts.justtrailit.R;

public class RegistarNota extends AppCompatActivity {

    public static final int CAMARA_REQUEST = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar_nota);
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
            }
        }
    }
}