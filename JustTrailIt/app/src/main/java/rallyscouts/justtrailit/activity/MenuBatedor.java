package rallyscouts.justtrailit.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
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
import rallyscouts.justtrailit.business.Atividade;
import rallyscouts.justtrailit.business.Batedor;
import rallyscouts.justtrailit.business.JsonRC;
import rallyscouts.justtrailit.data.AtividadeDAO;
import rallyscouts.justtrailit.data.BatedorDAO;
import rallyscouts.justtrailit.data.MapaDAO;
import rallyscouts.justtrailit.data.NotaDAO;
import rallyscouts.justtrailit.data.VeiculoDAO;

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
    private MapaDAO mapas;
    private VeiculoDAO veiculos;
    private AtividadeDAO atividades;

    private String ipServer;
    private int portServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_batedor);

        this.batedores = new BatedorDAO(MenuBatedor.this);
        this.notas = new NotaDAO(MenuBatedor.this);
        this.mapas = new MapaDAO(MenuBatedor.this);
        this.veiculos = new VeiculoDAO(MenuBatedor.this);
        this.atividades = new AtividadeDAO(MenuBatedor.this);

        this.batedorLogin = batedores.getBatedor((String)getIntent().getExtras().get("email"));

        textView_AtividadeDisp = (TextView) findViewById(R.id.textView_Atividade);
        button_gerirAtividade = (Button) findViewById(R.id.button_gerirAtividade);
        button_download = (Button) findViewById(R.id.button_DownloadAtividade);
        button_upload = (Button) findViewById(R.id.button_uploadAtividade);

        this.ipServer = null;
        this.portServer = -1;

        if(batedorLogin.getAtividade()==-1){
            textView_AtividadeDisp.setText("Não existe nenhuma atividade em processamento");
            this.button_gerirAtividade.setEnabled(false);
            this.button_download.setEnabled(true);
            this.button_upload.setEnabled(false);
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

        if(ipServer!=null && portServer!=-1){
            AsyncTask taskDownload = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    try {

                        JSONObject request = JsonRC.downloadAtividade(batedorLogin.getEmail(),batedorLogin.getPassword());

                        Socket socket = new Socket(ipServer,portServer);
                        BufferedReader br = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                        BufferedWriter bw =  new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));

                        bw.write(request.toString());
                        bw.newLine();
                        bw.flush();

                        String json = br.readLine();
                        String res = JsonRC.reciveAtividade(MenuBatedor.this,json,batedorLogin);

                        Log.i(TAG,res);

                        socket.close();
                    } catch (IOException e) {

                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    if(batedorLogin.getAtividade()>=0) {
                        textView_AtividadeDisp.setText("Atividade " + batedorLogin.getAtividade() + " não enviada");
                        button_gerirAtividade.setEnabled(true);
                        button_download.setEnabled(false);
                        button_upload.setEnabled(true);
                    }else{
                        if(batedorLogin.getAtividade()==-1){
                            Toast.makeText(getApplicationContext(), "Não existe atividade disponivel" , Toast.LENGTH_LONG).show();
                        }
                    }

                }
            };

            taskDownload.execute();
        }else{
            configConnection(findViewById(R.id.button_configConnetion));
            Toast.makeText(getApplicationContext(), "Precisa de configurar a ligação", Toast.LENGTH_LONG).show();
        }
    }

    public void uploadAtividade(View v) {


        if (ipServer != null && portServer != -1) {

            AsyncTask taskUpload = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    JSONObject send = JsonRC.sendAtividade(batedorLogin, notas.getAllNotas(batedorLogin.getAtividade()));
                    try {
                        Log.i(TAG, send.toString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        Socket socket = new Socket(ipServer, portServer);

                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                        bw.write(send.toString());
                        bw.newLine();
                        bw.flush();


                        String jsonACK = br.readLine();

                        int ack = JsonRC.reciveACK(jsonACK);

                        if (ack == -2) {
                            notas.deleteAllNotaAtividade(batedorLogin.getAtividade());
                            veiculos.deleteAllVeiculoAtividade(batedorLogin.getAtividade());
                            mapas.deleteMapa(batedorLogin.getAtividade());
                            atividades.deleteAtividade(batedorLogin.getAtividade());
                            batedorLogin.setAtividade(-1);
                            batedores.updateBatedor(batedorLogin);
                            Log.i(TAG, "Atividade enviada");
                        } else {
                            Log.i(TAG, "Não foi enviada corretamente a atividade");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    if (batedorLogin.getAtividade() == -1) {
                        button_gerirAtividade.setEnabled(false);
                        button_download.setEnabled(true);
                        button_upload.setEnabled(false);
                        textView_AtividadeDisp.setText("Não existe nenhuma atividade em processamento");
                    } else {
                        Toast.makeText(getApplicationContext(), "Não foi enviada corretamente a atividade", Toast.LENGTH_LONG).show();
                    }

                }
            };

            taskUpload.execute();
        }else {
            configConnection(findViewById(R.id.button_configConnetion));
            Toast.makeText(getApplicationContext(), "Precisa de configurar a ligação", Toast.LENGTH_LONG).show();
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
