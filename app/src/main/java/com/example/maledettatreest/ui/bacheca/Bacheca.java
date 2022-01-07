package com.example.maledettatreest.ui.bacheca;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.maledettatreest.databinding.FragmentBachecaBinding;
import com.example.maledettatreest.models.ApplicationModel;
import com.example.maledettatreest.network.VolleySingleton;
import com.example.maledettatreest.ui.adapter.Adapter_lines;
import com.example.maledettatreest.utils.Utils;

import org.json.JSONException;

public class Bacheca extends Fragment {

    FragmentBachecaBinding binding;
    private Adapter_lines adapter;
    String media;

    public Bacheca() {
        // Required empty public constructor
    }

    public static Bacheca newInstance() {

        return new Bacheca();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        binding.txtRitardo.setText(media);
        //definiamo il layout manager
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        //creiamo l'adapter e lo associamo alla recyclerView
        adapter = new Adapter_lines( this, getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBachecaBinding.inflate(inflater, container, false);

        try {
            getLines();
            media = Utils.getMedia(this.getContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return binding.getRoot();
    }

    private void getLines() throws JSONException {
        String url = "https://ewserver.di.unimi.it/mobicomp/treest/getLines.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, Utils.getSessionIDJsonRequest(this.getContext()),
                response -> {
                    try {
                        ApplicationModel.getInstance().initFromJson(response);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    binding.recyclerView.setAdapter(adapter);
                }, error -> {
            Utils.showErrorNetwork(this.getContext());
            Log.e("spash activity", "Errore volley");
        });

        VolleySingleton.getInstance(this.getContext()).addToRequestQueue(jsonObjectRequest);
    }
}