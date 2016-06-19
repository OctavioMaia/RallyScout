package rallyscouts.justtrailit.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import rallyscouts.justtrailit.R;
import rallyscouts.justtrailit.business.Nota;
import rallyscouts.justtrailit.data.NotaDAO;

public class NotaDetails extends AppCompatActivity {

    public static final String TAG = "NotaDetails";
    public static final String ID_NOTA = "idNota";
    public static final String ID_ATIVIDADE = "idAtividade";

    private NotaDAO notas;
    private Nota nota;
    private AudioTrack audioTrack;

    private Button start,pause,stop;
    private TextView notaTextual;
    private LinearLayout tl;
    private SeekBar seekbar;


    private MediaPlayer mediaPlayer;
    private Handler myHandler = new Handler();;
    private double startTime = 0;
    private double finalTime = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota_details);

        this.notas = new NotaDAO(NotaDetails.this);
        this.nota = notas.getNota(getIntent().getExtras().getInt(ID_NOTA),getIntent().getExtras().getInt(ID_ATIVIDADE));

        this.setTitle("Nota: " + this.nota.getIdNota() +
                " -> " + this.nota.getLocalRegisto().getLatitude() +
                " : " + this.nota.getLocalRegisto().getLongitude()
        );

        this.notaTextual = (TextView) findViewById(R.id.textView_NotaTextual);
        this.tl = (LinearLayout) findViewById(R.id.imagensLayout);
        this.start = (Button) findViewById(R.id.button_Play);
        this.pause = (Button) findViewById(R.id.button_Pause);
        this.stop = (Button) findViewById(R.id.button_Stop);
        this.seekbar = (SeekBar) findViewById(R.id.seekBar);

        if(nota==null){
            Toast.makeText(getApplicationContext(), "Nota null" , Toast.LENGTH_LONG).show();
        }

        String text = nota.getNotaTextual();
        if(text!=null){
            notaTextual.setText(text);
        }

        tl.removeAllViews();

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (final Bitmap bitmap : nota.getImagens() ) {
            ImageView iv = (ImageView) inflater.inflate(
                    R.layout.item_image, null);
            iv.setImageBitmap(bitmap);
            iv.setLayoutParams( new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            tl.addView(iv);
        }

        if( nota.getVoice()!=null && nota.getVoice().length>0){
            pause.setEnabled(false);
            stop.setEnabled(false);

            start.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {

                    int minBufferSize = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_8BIT);

                    audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
                            AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_8BIT,
                            minBufferSize, AudioTrack.MODE_STREAM);

                    audioTrack.setStereoVolume(1.0f,1.0f);

                    audioTrack.setVolume(audioTrack.getMaxVolume());

                    audioTrack.play();



                    audioTrack.write(nota.getVoice(), 0, 2097152);

                    Log.i(TAG,"Bytes audio: " + new String(nota.getVoice()));

                    Toast.makeText(getApplicationContext(), "Play " + audioTrack.getPositionNotificationPeriod(),Toast.LENGTH_SHORT).show();

                    /*
                    mediaPlayer = playByteArray(nota.getVoice());

                    Toast.makeText(getApplicationContext(), "Playing sound",Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();

                   // seekbar.setProgress(mediaPlayer.getCurrentPosition());
                    //myHandler.postDelayed(UpdateSongTime,100);
                    */

                    pause.setEnabled(true);
                    stop.setEnabled(true);
                    start.setEnabled(false);

                }
            });


            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Pausing sound",Toast.LENGTH_SHORT).show();
                    audioTrack.pause();
                    stop.setEnabled(true);
                    start.setEnabled(true);
                    pause.setEnabled(false);
                }
            });


            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    audioTrack.stop();
                    audioTrack.release();
                    pause.setEnabled(false);
                    stop.setEnabled(false);
                    start.setEnabled(true);
                }
            });

        }else{
            //LinearLayout linearLayoutSound = (LinearLayout) findViewById(R.id.LinearLayout_Sound);
            //linearLayoutSound.removeAllViews();
            this.start.setEnabled(false);
            this.stop.setEnabled(false);
            this.pause.setEnabled(false);
            this.seekbar.setEnabled(false);
        }
    }

/*
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };*/


/*
    private MediaPlayer playByteArray(byte[] mp3SoundByteArray) {
        MediaPlayer mediaPlayer = null;
        try {
            File myTemp = File.createTempFile("testeSound", "mp3", getCacheDir());
            myTemp.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(myTemp);
            fos.write(mp3SoundByteArray);
            fos.close();

            mediaPlayer = new MediaPlayer();

            FileInputStream myFileSound = new FileInputStream(myTemp);

            mediaPlayer.setDataSource(myFileSound.getFD());

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });


        } catch (IOException ex) {
            Log.w(TAG,"Não foi possivel criar o ficheiro temporario de musica");
        }
        return mediaPlayer;
    }

*/

}
