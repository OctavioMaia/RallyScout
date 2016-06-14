package rallyscouts.justtrailit.activity;

import android.content.Intent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import rallyscouts.justtrailit.R;
import rallyscouts.justtrailit.business.Batedor;
import rallyscouts.justtrailit.business.JsonRC;
import rallyscouts.justtrailit.data.BatedorDAO;
import rallyscouts.justtrailit.data.NotaDAO;

public class MenuBatedor extends AppCompatActivity {

    private static final String TAG = "MenuBatedor";
    public static final int SERVER_CONFIG = 1;
    private TextView textView_AtividadeDisp;
    private Button button_gerirAtividade;
    private Button button_download;
    private Button button_upload;

    private Batedor batedorLogin;
    private BatedorDAO batedores;
    private NotaDAO notas;

    private String ipServer;
    private int portServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_batedor);

        this.batedores = new BatedorDAO(MenuBatedor.this);
        this.notas = new NotaDAO(MenuBatedor.this);
        this.batedorLogin = batedores.getBatedor((String)getIntent().getExtras().get("email"));


        textView_AtividadeDisp = (TextView) findViewById(R.id.textView_Atividade);
        button_gerirAtividade = (Button) findViewById(R.id.button_gerirAtividade);
        button_download = (Button) findViewById(R.id.button_DownloadAtividade);
        button_upload = (Button) findViewById(R.id.button_uploadAtividade);

        this.ipServer = null;
        this.portServer = -1;

        if(batedorLogin.getAtividade()==-1){
            textView_AtividadeDisp.setText("Não existe nenhuma atividade em processamento");
            //this.button_gerirAtividade.setEnabled(false);
            //this.button_download.setEnabled(true);
            //this.button_upload.setEnabled(false);
        }else{
            textView_AtividadeDisp.setText("Atividade " + (int)getIntent().getExtras().get("atividade") + " não enviada");
            this.button_gerirAtividade.setEnabled(true);
            this.button_download.setEnabled(false);
            this.button_upload.setEnabled(true);
        }
    }

    public void gerirAtividade(View v){
        Intent intentGerirAtividade = new Intent(MenuBatedor.this, GerirAtividade.class);
        intentGerirAtividade.putExtra("email",batedorLogin.getEmail());
        MenuBatedor.this.startActivity(intentGerirAtividade);
    }

    public void downloadAtividade(View v){


        Toast.makeText(getApplicationContext(), "IP: " + ipServer + " port: " + portServer , Toast.LENGTH_LONG).show();

        if(ipServer!=null && portServer!=-1){
            //Toast.makeText(getApplicationContext(), "Comunicação ainda não está a funcionar" + portServer , Toast.LENGTH_LONG).show();

            JSONObject request = JsonRC.downloadAtividade(batedorLogin.getEmail(),batedorLogin.getPassword());

            Toast.makeText(getApplicationContext(), request.toString() , Toast.LENGTH_LONG).show();

        }
        /*    try {
                Socket socket = new Socket(ipServer,portServer);

                // ler do socket a atividade
                BufferedReader br = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                br.readLine();

            } catch (IOException e) {
                Log.e(TAG,"Application can not connect to Server");
            }
        }
        */
    }

    public void uploadAtividade(View v){
        Toast.makeText(getApplicationContext(), "Comunicação ainda não está a funcionar" + portServer , Toast.LENGTH_LONG).show();
        if(ipServer!=null && portServer!=-1) {


            JSONObject send = JsonRC.sendAtividade(batedorLogin,notas.getAllNotas(batedorLogin.getAtividade()));

            Toast.makeText(getApplicationContext(), send.toString() , Toast.LENGTH_LONG).show();

            /*
            try {
                Socket socket = new Socket(ipServer,portServer);

                BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
                bw.write("ola");
                bw.newLine();
                bw.flush();
            } catch (IOException e) {
                Log.e(TAG,"Application can not connect to Server");
            }
        */
        }

    }

    public void configConnection(View v){
        Intent intentGerirAtividade = new Intent(MenuBatedor.this, ConnetionServer.class);
        MenuBatedor.this.startActivityForResult(intentGerirAtividade,SERVER_CONFIG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SERVER_CONFIG){
            if(resultCode == RESULT_OK){
                this.ipServer = (String) data.getExtras().get("ip");
                this.portServer = (int) data.getExtras().get("port");
            }
        }
    }
}
