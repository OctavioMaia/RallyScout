package rallyscouts.justtrailit.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
    }

    public void saveConfigs(View v){
        Intent intent = new Intent();
        intent.putExtra("ip",ip.getText().toString());
        intent.putExtra("port",Integer.parseInt(port.getText().toString()));
        setResult(RESULT_OK, intent);
        this.finish();
    }
}
