package com.example.maledettatreest.ui.adapter;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.maledettatreest.R;
import com.example.maledettatreest.models.ApplicationModel;
import com.example.maledettatreest.models.Post;
import com.example.maledettatreest.network.VolleySingleton;
import com.example.maledettatreest.ui.bacheca.BachecaPosts;
import com.example.maledettatreest.utils.Utils;

import org.json.JSONException;

public class Adapter_posts extends RecyclerView.Adapter<ViewHolderPosts>{

    private LayoutInflater mInflater;
    private Context context;
    private Application application;

    public Adapter_posts(BachecaPosts fragment, Context context, Application application) {
        this.mInflater = LayoutInflater.from(fragment);
        this.context = context;
        this.application = application;
    }

    // viene richiamato quando si crea un nuovo oggetto di view (che rappresenta una cella)
    @Override
    public ViewHolderPosts onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_row_posts, parent, false);
        return new ViewHolderPosts(view, context, application);
    }

    // viene richiamato quando un oggetto di view (che rappresenta una cella) viene associato ai suoi dati (nota che un
    //parametro del metodo è la posizione dell’oggetto nella lista)
    @Override
    public void onBindViewHolder(ViewHolderPosts holder, int position) {
        Post s = ApplicationModel.getInstance().getPost(position);
        holder.setText(s);

        holder.segui.setOnClickListener(v -> {

            if (s.followingAuthor){
                try {
                    unfollowPerson(s.author,holder.segui);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    followPerson(s.author,holder.segui);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //viene chiamato per sapere quanti elementi ci sono nella lista (model) da mostrare
    @Override
    public int getItemCount() {
        return ApplicationModel.getInstance().getSizePost();
    }

    private void followPerson(String uid, Button btn) throws JSONException {
        String url = "https://ewserver.di.unimi.it/mobicomp/treest/follow.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, Utils.getPictureJsonRequest(context, uid),
                response -> {
                    btn.setBackgroundColor(Color.parseColor("#CFE1A7"));
                    btn.setText("SEGUITO");
                    Utils.addFollowers(context);
                }, error -> {
            // TODO: Handle error
            Log.e("spash activity", "Errore volley");
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void unfollowPerson(String uid, Button btn) throws JSONException {
        String url = "https://ewserver.di.unimi.it/mobicomp/treest/unfollow.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, Utils.getPictureJsonRequest(context, uid),
                response -> {
                    btn.setBackgroundColor(Color.parseColor("#FFB6C1"));
                    btn.setText("SEGUI");
                    Utils.deleteFollowers(context);
                }, error -> {
            // TODO: Handle error
            Log.e("spash activity", "Errore volley");
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}

