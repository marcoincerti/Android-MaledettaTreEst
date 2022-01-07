package com.example.maledettatreest.ui.spashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.maledettatreest.MainActivity;
import com.example.maledettatreest.R;
import com.example.maledettatreest.databinding.ActivitySplashScreenBinding;
import com.example.maledettatreest.network.VolleySingleton;
import com.example.maledettatreest.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreenActivity extends AppCompatActivity {

    ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String url = "https://ewserver.di.unimi.it/mobicomp/treest/register.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response.has("sid")) {
                            try {
                                Utils.saveSessionID(getApplicationContext(), response.get("sid").toString());
                                showMainActivity();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                Log.e("spash activity", "Errore volley");
            }
        });

        // Add the request to the RequestQueue.
        if (Utils.getSessionID(this).equals("-1")) {
            VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        } else {
            showMainActivity();
        }
    }

    private void showMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}