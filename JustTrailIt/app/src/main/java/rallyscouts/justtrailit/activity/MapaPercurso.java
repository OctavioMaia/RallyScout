package rallyscouts.justtrailit.activity;


import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Map;

import rallyscouts.justtrailit.Manifest;
import rallyscouts.justtrailit.R;
import rallyscouts.justtrailit.business.Mapa;
import rallyscouts.justtrailit.data.MapaDAO;

public class MapaPercurso extends AppCompatActivity implements OnMapReadyCallback {


    private MapaDAO mapa;
    private Mapa mapaLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_percurso);

        this.mapa = new MapaDAO(MapaPercurso.this);
        this.mapaLocal = mapa.getMapa((int)getIntent().getExtras().get("idAtividade"));

        this.setTitle("Mapa da prova " + mapaLocal.getNomeProva());

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
