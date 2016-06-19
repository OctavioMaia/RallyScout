package rallyscouts.justtrailit.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.nfc.Tag;
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
    private AudioRecord audioRecorder;
    private File myTempAudio;


    private boolean isRecording;
    private static final int sizeMaxAudio =  2000000;
    private int bytesRead;
    private byte[] bufferAudio = new byte[sizeMaxAudio];
    private Thread recordingThread = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar_nota);

        this.notas = new NotaDAO(RegistarNota.this);

        this.isRecording = false;

        Location loc = new Location("");
        loc.setLatitude(getIntent().getExtras().getDouble(LOC_LATITUDE));
        loc.setLongitude(getIntent().getExtras().getDouble(LOC_LONGUITUDE));

        this.idAtividade = getIntent().getExtras().getInt(ID_ATIVIDADE);
        this.notaToSave = new Nota(getIntent().getExtras().getInt(ID_NOTA),loc);

        this.setTitle("Registar Nota: " + this.notaToSave.getIdNota() +
                " Lat: " + this.notaToSave.getLocalRegisto().getLatitude() +
                " Lng: " + this.notaToSave.getLocalRegisto().getLongitude()
        );

        this.mensagem = (EditText)findViewById(R.id.id_InserirTexto);

        this.startPause = (ImageButton) findViewById(R.id.imageButton_Start_PauseRecord);
        this.stop = (ImageButton) findViewById(R.id.imageButton_Stop);

        this.registarImagem = (Button) findViewById(R.id.button_RegistarImagem);
        this.analisarNota = (Button) findViewById(R.id.button_AnalisarNota);
        this.submit = (Button) findViewById(R.id.button_SubmissaoNota);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //audio sound premission request
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_MICROPHONE_RECORD_AUDIO);
            } else {
                this.startPause.setEnabled(true);
                this.stop.setEnabled(false);
                Log.d(TAG, "Already granted access to audio");
            }
            //camera premission request
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_MICROPHONE_RECORD_AUDIO);
            } else {
                this.startPause.setEnabled(true);
                this.stop.setEnabled(false);
                Log.d(TAG, "Already granted access to camera");
            }
        }
    }

    public void registarImagem(View v) {
        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camaraIntent,CAMARA_REQUEST);
    }

    public void confirmarSubmissao(View v) {
        notaToSave.setNotaTextual(mensagem.getText().toString());
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

                //Log.i(TAG,"tamanho da camera: " + camaraImage.getByteCount());

                //Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                  //      camaraImage, camaraImage.getWidth()/4, camaraImage.getHeight()/4, false);

               // Log.i(TAG,"tamanho novo: " + resizedBitmap.getByteCount());


               // if (resizedBitmap.getByteCount()>0){
                 //   Toast.makeText(getApplicationContext(), "Recebi a imagem com alguma coisa" , Toast.LENGTH_LONG).show();
                //}
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
                    this.startPause.setEnabled(true);
                    this.stop.setEnabled(false);
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

        this.startPause.setEnabled(false);
        this.stop.setEnabled(true);

        final int bufferSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_8BIT);

        this.audioRecorder = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                8000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_8BIT,
                bufferSize);

        audioRecorder.startRecording();
        isRecording = true;

        recordingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                bytesRead = 0;
                while (isRecording && bytesRead < sizeMaxAudio) {
                    // gets the voice output from microphone to byte format

                    bytesRead += audioRecorder.read(bufferAudio, 0, sizeMaxAudio);
                    Log.d(TAG,"Audio recive" + bufferAudio.toString());
                }
            }
        });

        recordingThread.start();
    }


    public void stopRecordAudio(View v){

        this.startPause.setEnabled(false);
        this.stop.setEnabled(false);

        audioRecorder.stop();
        isRecording = false;
        notaToSave.setVoice(bufferAudio);

        Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
    }
}
