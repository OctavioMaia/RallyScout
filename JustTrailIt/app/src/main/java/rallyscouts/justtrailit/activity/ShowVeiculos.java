package rallyscouts.justtrailit.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import rallyscouts.justtrailit.R;
import rallyscouts.justtrailit.adapters.VeiculoExpandableListAdapter;
import rallyscouts.justtrailit.data.VeiculoDAO;

public class ShowVeiculos extends AppCompatActivity {

    private VeiculoDAO veiculos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_veiculos);

        this.veiculos = new VeiculoDAO(ShowVeiculos.this);

        this.setTitle("Veiculos");

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandList);

        VeiculoExpandableListAdapter veiculosAdapter = new VeiculoExpandableListAdapter(
                this,
                veiculos.getAllVeiculos((int)getIntent().getExtras().get("idAtividade"))
        );

        listView.setAdapter(veiculosAdapter);
    }
}
