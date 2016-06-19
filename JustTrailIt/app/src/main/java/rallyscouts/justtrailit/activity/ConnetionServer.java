package rallyscouts.justtrailit.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;

import rallyscouts.justtrailit.R;

public class ConnetionServer extends AppCompatActivity {


    private EditText ip;
    private EditText port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connetion_server);

        this.setTitle("Configuração Conecção");

        this.ip = (EditText) findViewById(R.id.editText_adress);
        this.port = (EditText) findViewById(R.id.editText_port);
        port.setText("6969");

        /*
        this.ip.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER){
                    port.setSelection(port.getSelectionStart());
                    port.requestFocus();
                    return true;
                }
                return false;
            }
        });

        this.port.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER){
                    saveConfigs(v);
                    return true;
                }
                return false;
            }
        });*/
    }

    public void saveConfigs(View v){
        try {
            InetAddress.getByName(ip.getText().toString());
            Intent intent = new Intent();
            intent.putExtra("ip",ip.getText().toString());
            intent.putExtra("port",Integer.parseInt(port.getText().toString()));
            setResult(RESULT_OK, intent);
            this.finish();
        } catch (UnknownHostException e) {
            Toast.makeText(getApplicationContext(),"Invalid IP: " + ip.getText().toString() , Toast.LENGTH_LONG).show();
        }
    }
}
