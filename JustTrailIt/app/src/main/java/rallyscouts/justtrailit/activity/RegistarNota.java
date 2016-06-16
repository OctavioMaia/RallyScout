package rallyscouts.justtrailit.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.MediaRecorder;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import rallyscouts.justtrailit.R;
import rallyscouts.justtrailit.business.Nota;
import rallyscouts.justtrailit.data.NotaDAO;

public class RegistarNota extends AppCompatActivity {

    public static final String TAG = "RegistarNota";

    public static final String ID_ATIVIDADE = "idAtividade";
    public static final String ID_NOTA = "idNota";

    public static final String LOC_LATITUDE = "latitude";
    public static final String LOC_LONGUITUDE = "longuitude";

    private NotaDAO notas;

    private int idAtividade;
    private Nota notaToSave;

    public static final int CAMARA_REQUEST = 10;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 29;
    public static final int MY_PERMISSIONS_REQUEST_MICROPHONE_RECORD_AUDIO = 30;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 31;

    private EditText mensagem;

    private Button registarImagem,analisarNota,submit;
    private ImageButton startPause,stop;
    private MediaRecorder mediaRecorder;
    private File myTempAudio;

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
        this.notaToSave = new Nota(getIntent().getExtras().getInt(ID_NOTA),loc);

        this.setTitle("Registar Nota: " + idAtividade +
                " Lat: " + this.notaToSave.getLocalRegisto().getLatitude() +
                " Lng: " + this.notaToSave.getLocalRegisto().getLongitude()
        );

        this.mensagem = (EditText)findViewById(R.id.id_InserirTexto);

        this.registarImagem = (Button) findViewById(R.id.button_RegistarImagem);
        this.analisarNota = (Button) findViewById(R.id.button_AnalisarNota);
        this.submit = (Button) findViewById(R.id.button_SubmissaoNota);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //audio sound premission request
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_MICROPHONE_RECORD_AUDIO);
            } else {
                Log.d(TAG, "Already granted access to audio");
                initializeAudio();
            }
            //camera premission request
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_MICROPHONE_RECORD_AUDIO);
            } else {
                Log.d(TAG, "Already granted access to camera");
                initializeAudio();
            }
        }else{
            initializeAudio();
        }
    }


    public void registarImagem(View v) {
        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camaraIntent,CAMARA_REQUEST);
    }

    public void confirmarSubmissao(View v) {
        //Falta definir a voz a latitude e a longitude.
        notaToSave.setNotaTextual(mensagem.getText().toString());
        notas.insertNota(idAtividade,notaToSave);
        finish();
    }

    private void initializeAudio(){
        try {
            myTempAudio = File.createTempFile("createSound", "mp3", getCacheDir());
            myTempAudio.deleteOnExit();

            FileInputStream myFileSound = new FileInputStream(myTempAudio);

            this.startPause = (ImageButton) findViewById(R.id.imageButton_Start_PauseRecord);
            this.stop = (ImageButton) findViewById(R.id.imageButton_Stop);
            this.stop.setEnabled(false);
            mediaRecorder=new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setOutputFile(myFileSound.getFD());
        } catch (IOException e) {
            this.stop.setEnabled(false);
            this.startPause.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            if(requestCode==CAMARA_REQUEST)
            {
                Bitmap camaraImage = (Bitmap) data.getExtras().get("data");

                if (camaraImage.getByteCount()>0){
                    Toast.makeText(getApplicationContext(), "Recebi a imagem com alguma coisa" , Toast.LENGTH_LONG).show();

                }
                notaToSave.addImagem(camaraImage);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_MICROPHONE_RECORD_AUDIO: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission Granted");
                    initializeAudio();
                } else {
                    Log.d(TAG, "Permission Failed");
                    Toast.makeText(getApplicationContext(), "You must allow permission record audio to your mobile device.", Toast.LENGTH_SHORT).show();
                    startPause.setEnabled(false);
                    stop.setEnabled(false);
                }
            }
            case MY_PERMISSIONS_REQUEST_CAMERA : {

            }
        }
    }

    public void startRecordAudio(View v){
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            this.startPause.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void stopRecordAudio(View v){
        this.startPause.setEnabled(false);
        this.stop.setEnabled(false);

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder  = null;

        Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
    }
}
