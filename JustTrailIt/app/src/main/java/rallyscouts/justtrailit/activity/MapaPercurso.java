package rallyscouts.justtrailit.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import rallyscouts.justtrailit.R;

public class MapaPercurso extends AppCompatActivity {

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_percurso);

        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();



    }
}
