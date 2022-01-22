package com.example.maledettatreest.ui.bacheca;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.maledettatreest.R;
import com.example.maledettatreest.databinding.ActivityBachecaPostsBinding;
import com.example.maledettatreest.models.ApplicationModel;
import com.example.maledettatreest.network.VolleySingleton;
import com.example.maledettatreest.ui.adapter.Adapter_posts;
import com.example.maledettatreest.utils.Utils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONException;

public class BachecaPosts extends AppCompatActivity {

    private String did;
    private String did_inverso;
    private String partenza;
    private String arrivo;

    private ActivityBachecaPostsBinding binding;
    private Adapter_posts adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bacheca_posts);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        binding = ActivityBachecaPostsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            did = extras.getString("key");
            partenza = extras.getString("partenza");
            arrivo = extras.getString("arrivo");
            did_inverso = extras.getString("did_inverso");
        }

        updateUI();

        try {
            getPosts();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        binding.cambio.setOnClickListener(view1 -> {
            String cambio = did;
            did = did_inverso;
            did_inverso = cambio;
            cambio = partenza;
            partenza = arrivo;
            arrivo = cambio;

            updateUI();

            try {
                getPosts();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        binding.fab.setOnClickListener(view12 -> {
            Intent intent = new Intent(BachecaPosts.this, InserisiciActivity.class);
            intent.putExtra("did", did);
            intent.putExtra("partenza", partenza);
            intent.putExtra("arrivo", arrivo);
            startActivity(intent);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        //definiamo il layout manager
        binding.recyclerViewPost.setLayoutManager(new LinearLayoutManager(this));

        //creiamo l'adapter e lo associamo alla recyclerView
        adapter = new Adapter_posts(this, this, getApplication());
    }

    private void getPosts() throws JSONException {
        String url = "https://ewserver.di.unimi.it/mobicomp/treest/getPosts.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, Utils.getStationJsonRequest(this, did),
                response -> {
                    try {
                        ApplicationModel.getInstance().initPostFromJson(response, getApplicationContext());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    binding.recyclerViewPost.setAdapter(adapter);
                }, error -> {
            Utils.showErrorNetwork(this);
            Log.e("spash activity", "Errore volley");
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void updateUI(){
        binding.partenza.setText(partenza);
        binding.arrivo.setText(arrivo);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}