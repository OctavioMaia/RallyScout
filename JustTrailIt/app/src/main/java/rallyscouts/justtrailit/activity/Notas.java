package rallyscouts.justtrailit.activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
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

public class Notas extends AppCompatActivity implements OnMapReadyCallback {

    public static final String ID_ATIVIDADE = "idAtividade";
    public static final String TAG = "Notas";
    private NotaDAO notas;
    private MapaDAO mapas;

    private List<Nota> notasShow;

    private GoogleMap googleMap;
    private  Map<Integer,Location> mapaCoords;
    private Marker mak;

    private Button analisarNota;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        this.setTitle("Notas");

        this.notas = new NotaDAO(Notas.this);
        this.mapas = new MapaDAO(Notas.this);
        this.notasShow = notas.getAllNotas(getIntent().getExtras().getInt(ID_ATIVIDADE));

        this.analisarNota = (Button) findViewById(R.id.button_AnalisarNota);
        this.spinner = (Spinner) findViewById(R.id.notas_spinner);

        // Spinner click listener

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, prepareSpinner());

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        this.mapaCoords = mapas.getMapa(getIntent().getExtras().getInt(ID_ATIVIDADE)).getCoord();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Integer item = (Integer) parentView.getItemAtPosition(position);
                addMarker( notasShow.get(position).getLocalRegisto(),
                        notasShow.get(position).getIdNota() +
                                " -> " + notasShow.get(position).getLocalRegisto().getLatitude() +
                                ":" + notasShow.get(position).getLocalRegisto().getLongitude()
                );
                Toast.makeText(getApplicationContext(), "Selected: " + position, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapNota);
        mapFragment.getMapAsync(this);
    }

    private ArrayList<String> prepareSpinner(){
        ArrayList<String> res = new ArrayList<>();
        if(notasShow!=null){
            for (Nota n : notasShow ) {
                res.add(n.getIdNota() + "->" + n.getLocalRegisto().getLatitude() + ":" + n.getLocalRegisto().getLongitude());
            }
        }
        return res;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

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
        route.geodesic(true);

        for(int i = 0;i<mapaCoords.size();i++){
            route.add( new LatLng(
                    mapaCoords.get( i ).getLatitude(),
                    mapaCoords.get( i ).getLongitude() ));
        }
        googleMap.addPolyline( route );
    }

    private void addMarker(Location loc, String info){
        if (mak!=null){
            this.mak.remove();
        }
        LatLng ll = new LatLng( loc.getLatitude(), loc.getLongitude() );
        this.mak = googleMap.addMarker(new MarkerOptions()
                .position( ll )
                .title( info ));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom( ll, 15));

    }

    public void analisarNota(View v){
        Intent analisarNota = new Intent(Notas.this, NotaDetails.class);

        analisarNota.putExtra(NotaDetails.ID_NOTA,(int)spinner.getSelectedItemPosition());
        analisarNota.putExtra(NotaDetails.ID_ATIVIDADE,getIntent().getExtras().getInt(ID_ATIVIDADE));
        Notas.this.startActivity(analisarNota);
    }
}
