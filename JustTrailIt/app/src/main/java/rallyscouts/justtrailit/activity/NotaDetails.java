package rallyscouts.justtrailit.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rallyscouts.justtrailit.R;
import rallyscouts.justtrailit.business.Nota;

public class NotaDetails extends AppCompatActivity {

    private static final String TAG = "NotaDetails";

    private Nota nota;
    private MediaPlayer mediaPlayer;

    private Button start,pause,stop;
    private TextView notaTextual;
    private TableLayout tl;
    private SeekBar seekbar;

    private Handler myHandler = new Handler();;
    private double startTime = 0;
    private double finalTime = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota_details);

        this.nota = (Nota) getIntent().getExtras().getSerializable("nota");
        this.notaTextual = (TextView) findViewById(R.id.textView_NotaTextual);
        this.tl = (TableLayout) findViewById(R.id.imagensLayout);
        this.start = (Button) findViewById(R.id.button_Play);
        this.pause = (Button) findViewById(R.id.button_Pause);
        this.stop = (Button) findViewById(R.id.button_Stop);

        String text = nota.getNotaTextual();
        if(text!=null){
            notaTextual.setText(text);
        }

        tl.removeAllViews();

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (Bitmap bitmap : nota.getImagens() ) {
            ImageView iv = (ImageView) inflater.inflate(
                    R.layout.item_image, null);

            iv.setImageBitmap(bitmap);

            tl.addView(iv);
        }

        if(nota.getVoice().length>0){
            stop.setEnabled(false);

            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mediaPlayer = playByteArray(nota.getVoice());

                    Toast.makeText(getApplicationContext(), "Playing sound",Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();

                    seekbar.setProgress(mediaPlayer.getCurrentPosition());
                    myHandler.postDelayed(UpdateSongTime,100);
                    pause.setEnabled(true);
                    start.setEnabled(false);
                }
            });


            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Pausing sound",Toast.LENGTH_SHORT).show();
                    mediaPlayer.pause();
                    stop.setEnabled(true);
                    start.setEnabled(true);
                    pause.setEnabled(false);
                }
            });


            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.seekTo(0);
                }
            });

        }else{
            LinearLayout linearLayoutSound = (LinearLayout) findViewById(R.id.LinearLayout_Sound);
            linearLayoutSound.removeAllViews();
        }
    }


    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };




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

        } catch (IOException ex) {
            Log.w(TAG,"Não foi possivel criar o ficheiro temporario de musica");
        }
        return mediaPlayer;
    }

}
