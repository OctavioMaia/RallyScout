package rallyscouts.justtrailit.activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rallyscouts.justtrailit.R;
import rallyscouts.justtrailit.business.Nota;
import rallyscouts.justtrailit.data.MapaDAO;
import rallyscouts.justtrailit.data.NotaDAO;

public class Notas extends AppCompatActivity implements OnMapReadyCallback,AdapterView.OnItemSelectedListener {

    private NotaDAO notas;
    private MapaDAO mapas;

    private List<Nota> notasShow;

    private GoogleMap googleMap;
    private  Map<Integer,Location> mapaCoords;
    private Marker mak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        this.setTitle("Notas");

        this.notas = new NotaDAO(Notas.this);
        this.mapas = new MapaDAO(Notas.this);
        this.notasShow = notas.getAllNotas(getIntent().getExtras().getInt("idAtividade"));

        Spinner spinner = (Spinner) findViewById(R.id.notas_spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Creating adapter for spinner
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, prepareSpinner());

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        this.mapaCoords = mapas.getMapa(getIntent().getExtras().getInt("idAtividade")).getCoord();
    }

    private ArrayList<Integer> prepareSpinner(){
        ArrayList<Integer> res = new ArrayList<>();
        if(notasShow!=null){
            for (Nota n : notasShow ) {
                res.add(n.getIdNota());
            }
        }
        return res;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng begin = new LatLng(mapaCoords.get(0).getLatitude(), mapaCoords.get(0).getLongitude());
        LatLng end = new LatLng(mapaCoords.get(mapaCoords.size()-1).getLatitude(), mapaCoords.get(mapaCoords.size()-1).getLongitude());

        googleMap.addMarker(new MarkerOptions()
                .position( begin )
                .title("BEGIN"));


        googleMap.addMarker(new MarkerOptions()
                .position( end )
                .title("END"));

        //ver melhor isto o zoom posso ter de colocar de acordo com a distancia do inicio ao fim
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom( begin, 15));

        PolylineOptions route = new PolylineOptions();

        route.color( Color.parseColor("#CC0000FF") );
        route.width( 5 );
        route.visible( true );



        for (Integer ord : mapaCoords.keySet() ) {
            route.add( new LatLng(
                    mapaCoords.get( ord ).getLatitude(),
                    mapaCoords.get( ord ).getLongitude() ));
        }

        googleMap.addPolyline( route );
    }

    public void addMarker(Location loc, String info){
        if (mak!=null){
            this.mak.remove();
        }
        this.mak = googleMap.addMarker(new MarkerOptions()
                .position( new LatLng( loc.getLatitude(), loc.getLongitude() ) )
                .title( info ));
    }

    public void analisarNota(View v){
        Spinner spinner = (Spinner) findViewById(R.id.notas_spinner);

        Intent analisarNota = new Intent(Notas.this, Notas.class);
        analisarNota.putExtra("nota",notasShow.get(spinner.getSelectedItemPosition()));
        Notas.this.startActivity(analisarNota);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
