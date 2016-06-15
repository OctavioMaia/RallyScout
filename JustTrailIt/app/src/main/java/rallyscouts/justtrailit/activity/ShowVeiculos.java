package rallyscouts.justtrailit.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import rallyscouts.justtrailit.R;
import rallyscouts.justtrailit.adapters.VeiculoExpandableListAdapter;
import rallyscouts.justtrailit.data.VeiculoDAO;

public class ShowVeiculos extends ListActivity {

    private VeiculoDAO veiculos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_veiculos);

        this.veiculos = new VeiculoDAO(ShowVeiculos.this);


        this.setTitle("Veiculos");

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.lvExp);

        VeiculoExpandableListAdapter veiculosAdapter = new VeiculoExpandableListAdapter(
                this,
                veiculos.getAllVeiculos((int)getIntent().getExtras().get("idAtiviade"))
        );

        listView.setAdapter(veiculosAdapter);
    }
}
