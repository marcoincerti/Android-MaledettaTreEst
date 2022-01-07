package com.example.maledettatreest.ui.bacheca;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.maledettatreest.R;
import com.example.maledettatreest.network.VolleySingleton;
import com.example.maledettatreest.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String did;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            did = extras.getString("key");
            //The key argument here must match that used in the other activity
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            getStations();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getStations() throws JSONException {
        String url = "https://ewserver.di.unimi.it/mobicomp/treest/getStations.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, Utils.getStationJsonRequest(this, did),
                response -> {
                    try {
                        JSONArray json = response.getJSONArray("stations");

                        int length = json.length();

                        for (int i = 0; i < length; i++) {

                            JSONObject stazione = json.getJSONObject(i);

                            LatLng sydney = new LatLng(stazione.getDouble("lat"), stazione.getDouble("lon"));

                            mMap.addMarker(new MarkerOptions()
                                    .position(sydney)
                                    .title(stazione.getString("sname")));

                            if (i == 0) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
                                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
                            }

                        }

                        Log.i("ciao", response.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Utils.showErrorNetwork(this);
            Log.e("spash activity", "Errore volley");
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}