package com.example.maledettatreest.ui.adapter;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.maledettatreest.R;
import com.example.maledettatreest.models.ApplicationModel;
import com.example.maledettatreest.models.PhotoModel;
import com.example.maledettatreest.models.Post;
import com.example.maledettatreest.network.VolleySingleton;
import com.example.maledettatreest.utils.Utils;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewHolderPosts extends RecyclerView.ViewHolder {

    public final TextView ritado;
    public final TextView data;
    public final TextView nome_utente;
    public final TextView stato;
    public final TextView commento;
    public final Button segui;
    public final ImageView picture;
    private final Context context;
    private final Application application;
    private final String uid;

    public ViewHolderPosts(View itemView, Context context, Application application) {
        super(itemView);
        this.context = context;
        this.application = application;
        ritado = itemView.findViewById(R.id.ritardo);
        data = itemView.findViewById(R.id.orario);
        nome_utente = itemView.findViewById(R.id.nome_utente_post);
        stato = itemView.findViewById(R.id.stato);
        commento = itemView.findViewById(R.id.commento);
        segui = itemView.findViewById(R.id.btn_segui);
        picture = itemView.findViewById(R.id.imageView2);
        uid = ApplicationModel.getInstance().getUid();
    }

    public void setText(Post s) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = dateFormat.parse(s.datetime);
            dateFormat = new SimpleDateFormat("EEEE 'alle' HH:mm");
            data.setText(dateFormat.format(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            getPicture(s.author, s.pversion);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (s.delay == null) {
            s.delay = "0";
        }
        if (s.status == null) {
            s.status = "0";
        }

        if ("1".equals(s.delay)) {
            ritado.setText("Ritardo di pochi minuti");
            ritado.setTextColor(Color.RED);
        } else if ("2".equals(s.delay)) {
            ritado.setText("Ritardo oltre i 15 minuti");
            ritado.setTextColor(Color.RED);
        } else if ("3".equals(s.delay)) {
            ritado.setText("Treni soppressi");
            ritado.setTextColor(Color.RED);
        } else {
            ritado.setText("In orario");
            ritado.setTextColor(Color.DKGRAY);
        }

        if ("1".equals(s.status)) {
            stato.setText("Accettabile");
            stato.setTextColor(Color.DKGRAY);
        } else if ("2".equals(s.status)) {
            stato.setText("Gravi problemi per i passeggeri");
            stato.setTextColor(Color.RED);
        } else {
            stato.setText("Situazione ideale");
            stato.setTextColor(Color.DKGRAY);
        }

        nome_utente.setText(s.authorName);
        commento.setText(s.comment);

        if (s.author.equals(uid)) {
            segui.setVisibility(View.GONE);
        } else {
            if (s.followingAuthor) {
                segui.setBackgroundColor(Color.parseColor("#CFE1A7"));
                segui.setText("SEGUITO");
            }
        }
    }

    private void getPicture(String uid, String pversion) throws JSONException {
        String uidPversion = uid + pversion;

        if (ApplicationModel.getInstance().checkPhoto(uidPversion) != null) {
            this.picture.setImageBitmap(Utils.fromBase64ToBitmap(ApplicationModel.getInstance().checkPhoto(uidPversion), this.context));
        } else if (PhotoModel.getInstance(this.application).checkPhotoinDB(uidPversion)) {
            String base64 = PhotoModel.getInstance(this.application).getPhototoDB(uidPversion);
            this.picture.setImageBitmap(Utils.fromBase64ToBitmap(base64, this.context));
            ApplicationModel.getInstance().insertPhotoinMap(uidPversion, base64);
        } else {
            getPictureFromUrl(uid);
        }
    }

    private void getPictureFromUrl(String uid) throws JSONException {
        String url = "https://ewserver.di.unimi.it/mobicomp/treest/getUserPicture.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, Utils.getPictureJsonRequest(context, uid),
                response -> {
                    try {
                        if ((response.getString("picture").equals("null"))) {
                            picture.setImageDrawable(context.getResources().getDrawable(R.drawable.placeholder));
                        } else {
                            picture.setImageBitmap(Utils.fromBase64ToBitmap(response.getString("picture"), this.context));

                            String key = response.getString("uid") + response.getString("pversion");
                            String value = response.getString("picture");
                            ApplicationModel.getInstance().insertPhotoinMap(key, value);
                            PhotoModel.getInstance(this.application).insertPhotoinDB(key, value);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            // TODO: Handle error
            Log.e("spash activity", "Errore volley");
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

}