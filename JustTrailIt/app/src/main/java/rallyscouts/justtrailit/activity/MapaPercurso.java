package rallyscouts.justtrailit.activity;


import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Map;

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

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));

        PolylineOptions route = new PolylineOptions();

        route.color( Color.parseColor("#CC0000FF") );
        route.width( 5 );
        route.visible( true );

        Map<Integer,Location> coords = mapaLocal.getCoord();

        for (Integer ord : coords.keySet() ) {
            route.add( new LatLng(
                    coords.get( ord ).getLatitude(),
                    coords.get( ord ).getLongitude() ));
        }

        googleMap.addPolyline( route );

    }


}
