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
            textView_AtividadeDisp.setText("Atividade " + batedorLogin.getAtividade() + " não enviada");
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


    /*
    Falta colocar aqui toda a interação com a tividade dos buttons
     */
    public void downloadAtividade(View v){


        Toast.makeText(getApplicationContext(), "IP: " + ipServer + " port: " + portServer , Toast.LENGTH_LONG).show();

        if(ipServer!=null && portServer!=-1){
            //Toast.makeText(getApplicationContext(), "Comunicação ainda não está a funcionar" + portServer , Toast.LENGTH_LONG).show();

            JSONObject request = JsonRC.downloadAtividade(batedorLogin.getEmail(),batedorLogin.getPassword());

            Toast.makeText(getApplicationContext(), request.toString() , Toast.LENGTH_LONG).show();

            String json = "{\n" +
                    "  \"idAtividade\": 1,\n" +
                    "  \"email\": \"rui@gmail.com\",\n" +
                    "  \"nomeEquipa\": \"KTM\",\n" +
                    "  \"mapa\": {\n" +
                    "    \"nomeProva\": \"1234\",\n" +
                    "    \"percurso\": [\n" +
                    "      {\n" +
                    "        \"lat\": 41.825178,\n" +
                    "        \"log\": -7.791377\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"lat\": 41.825378,\n" +
                    "        \"log\": -7.790517\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"lat\": 41.825928,\n" +
                    "        \"log\": -7.789952\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"lat\": 41.825724,\n" +
                    "        \"log\": -7.789437\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"lat\": 41.825559,\n" +
                    "        \"log\": -7.788663\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"lat\": 41.825463,\n" +
                    "        \"log\": -7.788519\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"lat\": 41.82561,\n" +
                    "        \"log\": -7.788276\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"lat\": 41.825765,\n" +
                    "        \"log\": -7.788507\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"lat\": 41.826891,\n" +
                    "        \"log\": -7.788432\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  \"veiculos\": [\n" +
                    "    {\n" +
                    "      \"chassi\": \"a\",\n" +
                    "      \"caracteristicas\": [\n" +
                    "        \"d\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"chassi\": \"312\",\n" +
                    "      \"caracteristicas\": [\n" +
                    "        \"d1\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"chassi\": \"das\",\n" +
                    "      \"caracteristicas\": [\n" +
                    "        \"d11231\"\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";


            JsonRC.reciveAtividade(MenuBatedor.this,json,batedorLogin);



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

    /*
        Falta colocar aqui toda a interação com a tividade dos buttons
    */
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
