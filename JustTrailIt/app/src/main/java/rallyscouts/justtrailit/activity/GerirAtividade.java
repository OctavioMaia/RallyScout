package rallyscouts.justtrailit.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import java.util.Map;

import rallyscouts.justtrailit.R;
import rallyscouts.justtrailit.business.Atividade;
import rallyscouts.justtrailit.business.Batedor;
import rallyscouts.justtrailit.business.Mapa;
import rallyscouts.justtrailit.data.AtividadeDAO;
import rallyscouts.justtrailit.data.BatedorDAO;
import rallyscouts.justtrailit.data.MapaDAO;
import rallyscouts.justtrailit.data.NotaDAO;

public class GerirAtividade extends AppCompatActivity implements OnMapReadyCallback {


    private BatedorDAO batedores;
    private AtividadeDAO atividades;
    private NotaDAO notas;
    private MapaDAO mapas;

    private Atividade atividadeAProcess;
    private Batedor batedorLogin;
    private Mapa mapaLocal;

    private GoogleMap googleMap;
    private TextView equipa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerir_atividade);

        batedores = new BatedorDAO(GerirAtividade.this);
        atividades = new AtividadeDAO(GerirAtividade.this);

        equipa = (TextView) findViewById(R.id.id_Equipa);

        batedorLogin = batedores.getBatedor((String)getIntent().getExtras().get("email"));
        atividadeAProcess = atividades.getAtividade(batedorLogin.getAtividade());

        this.setTitle("Gerir Atividade " + this.atividadeAProcess.getIdAtividade());

        equipa.setText("Equipa: " + atividadeAProcess.getNomeEquipa());

        this.mapas = new MapaDAO(GerirAtividade.this);
        this.mapaLocal = mapas.getMapa(batedorLogin.getAtividade());

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void verVeiculos(View v){
        Intent verVeiculos = new Intent(GerirAtividade.this, ShowVeiculos.class);
        verVeiculos.putExtra("idAtividade",atividadeAProcess.getIdAtividade());
        GerirAtividade.this.startActivity(verVeiculos);
    }

    public void registarNota(View v){

        Intent registarnota = new Intent(GerirAtividade.this, RegistarNota.class);
        registarnota.putExtra("idAtividade",batedorLogin.getAtividade());
        registarnota.putExtra("idNota",notas.getMaiorNota());
        registarnota.putExtra("location",new Location(""));
        GerirAtividade.this.startActivity(registarnota);

    }

    public void verNotas(View v){

        Intent vernotas = new Intent(GerirAtividade.this, Notas.class);
        //vernotas.putParcelableArrayListExtra("notas",(ArrayList<? extends Parcelable>) notas.getAllNotas(batedorLogin.getAtividade()));
        vernotas.putExtra("idAtividade",batedorLogin.getAtividade());
        GerirAtividade.this.startActivity(vernotas);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Map<Integer,Location> coords = mapaLocal.getCoord();

        LatLng begin = new LatLng(coords.get(0).getLatitude(), coords.get(0).getLongitude());
        LatLng end = new LatLng(coords.get(coords.size()-1).getLatitude(), coords.get(coords.size()-1).getLongitude());

        googleMap.addMarker(new MarkerOptions()
                .position( begin )
                .title("BEGIN"));


        googleMap.addMarker(new MarkerOptions()
                .position( end )
                .title("END"));

        //ver melhor isto o zoom posso ter de colocar de acordo com a distancia do inicio ao fim
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom( begin, 15));

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }

        PolylineOptions route = new PolylineOptions();

        route.color( Color.parseColor("#CC0000FF") );
        route.width( 5 );
        route.visible( true );

        for (Integer ord : coords.keySet() ) {
            route.add( new LatLng(
                    coords.get( ord ).getLatitude(),
                    coords.get( ord ).getLongitude() ));
        }

        googleMap.addPolyline( route );

    }

}
