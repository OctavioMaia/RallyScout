package rallyscouts.justtrailit.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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

    private static final float distanciaMinima = 900000000;

    private BatedorDAO batedores;
    private AtividadeDAO atividades;
    private NotaDAO notas;
    private MapaDAO mapas;

    private Atividade atividadeAProcess;
    private Batedor batedorLogin;
    private Mapa mapaLocal;

    private GoogleMap googleMap;
    private TextView equipa;
    private SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerir_atividade);

        this.batedores = new BatedorDAO(GerirAtividade.this);
        this.atividades = new AtividadeDAO(GerirAtividade.this);
        this.mapas = new MapaDAO(GerirAtividade.this);
        this.notas = new NotaDAO(GerirAtividade.this);

        this.batedorLogin = batedores.getBatedor((String) getIntent().getExtras().get("email"));
        this.atividadeAProcess = atividades.getAtividade(batedorLogin.getAtividade());
        this.mapaLocal = mapas.getMapa(batedorLogin.getAtividade());

        this.setTitle("Gerir Atividade " + this.atividadeAProcess.getIdAtividade());

        this.equipa = (TextView) findViewById(R.id.id_Equipa);
        this.equipa.setText("Equipa: " + atividadeAProcess.getNomeEquipa());

        this.search = (SearchView) findViewById(R.id.searchView);




        this.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    Intent intentSearch = new Intent(Intent.ACTION_WEB_SEARCH);
                    intentSearch.putExtra(SearchManager.USER_QUERY, search.getQuery());
                    Toast.makeText(getApplicationContext(), search.getQuery() , Toast.LENGTH_LONG).show();
                    startActivity(intentSearch);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Não foi possivel executar a query por " + search.getQuery() , Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    public void verVeiculos(View v) {
        Intent verVeiculos = new Intent(GerirAtividade.this, ShowVeiculos.class);
        verVeiculos.putExtra("idAtividade", atividadeAProcess.getIdAtividade());
        GerirAtividade.this.startActivity(verVeiculos);
    }

    public void registarNota(View v) {
        //verificar se a localização atual está proxima de algum ponto do percurso
        Toast.makeText(getApplicationContext(),""+  ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) , Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),""+  ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) , Toast.LENGTH_LONG).show();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location locGPS = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if(locGPS!=null){
               // Toast.makeText(getApplicationContext(),""+  locGPS.getLatitude() , Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),""+  locGPS.getLongitude() , Toast.LENGTH_LONG).show();


                //verificar se está muito longe de alguma posição do mapa
                // ir buscar qual é o ponto do mapa de que estamos mais proximos
                float maxDistance = distanciaMinima;
                Location locProxima = null;
                for ( Location locMapa : mapaLocal.getCoord().values()) {
                    double dist = locMapa.distanceTo(locGPS);
                    if( dist < maxDistance) {
                        maxDistance =(float)dist;
                        locProxima = locMapa;
                    }
                }

                if( locProxima!=null ) {
                    Intent registarnota = new Intent(GerirAtividade.this, RegistarNota.class);
                    registarnota.putExtra(RegistarNota.ID_ATIVIDADE,batedorLogin.getAtividade());
                    registarnota.putExtra(RegistarNota.ID_NOTA,notas.getLargerID(batedorLogin.getAtividade())+1);
                    registarnota.putExtra(RegistarNota.LOC_LATITUDE,locProxima.getLatitude());
                    registarnota.putExtra(RegistarNota.LOC_LONGUITUDE,locProxima.getLongitude());
                    GerirAtividade.this.startActivity(registarnota);
                }else{
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                    dlgAlert.setMessage("Está muito longe do percurso para realizar uma nota");
                    dlgAlert.setTitle("Location GPS");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.create().show();
                }
            }
        }else{
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Necessita de ativar o GPS");
            dlgAlert.setTitle("Location GPS");
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GerirAtividade.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }


    }

    public void verNotas(View v){

        Intent vernotas = new Intent(GerirAtividade.this, Notas.class);
        vernotas.putExtra(Notas.ID_ATIVIDADE,batedorLogin.getAtividade());
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
