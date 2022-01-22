package com.example.maledettatreest.ui.bacheca;

import static com.github.razir.progressbutton.ProgressButtonHolderKt.bindProgressButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.maledettatreest.R;
import com.example.maledettatreest.databinding.ActivityBachecaPostsBinding;
import com.example.maledettatreest.databinding.ActivityInserisiciBinding;
import com.example.maledettatreest.databinding.FragmentProfiloBinding;
import com.example.maledettatreest.network.VolleySingleton;
import com.example.maledettatreest.utils.Utils;
import com.github.razir.progressbutton.DrawableButton;
import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;

import kotlin.Unit;

public class InserisiciActivity extends AppCompatActivity {
    public String did;
    public String partenza;
    public String arrivo;
    public int delay = 0;
    public int status = 0;

    ActivityInserisiciBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserisici);

        binding = ActivityInserisiciBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            did = extras.getString("did");
            partenza = extras.getString("partenza");
            arrivo = extras.getString("arrivo");
        }

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        binding.partenzaInserisci.setText(partenza);
        binding.arrivoInserisci.setText(arrivo);

        bindProgressButton(this, binding.buttonSaveAddPost);
        binding.buttonSaveAddPost.setTextColor(Color.WHITE);

        binding.buttonSaveAddPost.setOnClickListener(v -> {
            showProgressLeft(binding.buttonSaveAddPost);
            try {
                addPost();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }

    private void showProgressLeft(final Button button) {
        DrawableButtonExtensionsKt.showProgress(button, progressParams -> {
            progressParams.setButtonTextRes(R.string.loading);
            progressParams.setProgressColor(Color.WHITE);
            progressParams.setGravity(DrawableButton.GRAVITY_TEXT_START);
            return Unit.INSTANCE;
        });
        button.setEnabled(false);

        new Handler().postDelayed(() -> {
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, R.string.saved);
        }, 3000);
    }

    private void btn_Enable(boolean active) {
        binding.buttonSaveAddPost.setEnabled(active);
        if (active) {
            binding.buttonSaveAddPost.setBackgroundColor(getResources().getColor(R.color.shrine_pink_100));
            binding.buttonSaveAddPost.setText("PUBBLICA");
        } else {
            binding.buttonSaveAddPost.setBackgroundColor(Color.LTGRAY);
        }
    }

    private void addPost() throws JSONException {
        String url = "https://ewserver.di.unimi.it/mobicomp/treest/addPost.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, Utils.addPost(this, did,delay,status, binding.txtCommento.getText().toString()),
                response -> {
                    Log.i("ciao", response.toString() + " ok");
                    DrawableButtonExtensionsKt.hideProgress(binding.buttonSaveAddPost, R.string.saved);
                    btn_Enable(false);
                    new MaterialAlertDialogBuilder(this)
                            .setTitle("Ci sei riuscito")
                            .setMessage("Hai pubblicato il tuo post!")
                            .setPositiveButton("Chiudi", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }).show();
                }, error -> {
            Utils.showErrorNetwork(this);
            Log.e("spash activity", "Errore volley");
            DrawableButtonExtensionsKt.hideProgress(binding.buttonSaveAddPost, R.string.saved);
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void onRadioButtonClickedRitardo(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        int id = view.getId();
        if (id == R.id.radio_button_1) {
            if (checked)
                delay = 0;
        } else if (id == R.id.radio_button_2) {
            if (checked)
                delay = 1;
        } else if (id == R.id.radio_button_3) {
            if (checked)
                delay = 2;
        } else if (id == R.id.radio_button_4) {
            if (checked)
                delay = 3;
        }
    }

    public void onRadioButtonClickedStatus(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        int id = view.getId();
        if (id == R.id.radio_button_status_0) {
            if (checked)
                status = 0;
        } else if (id == R.id.radio_button_status_1) {
            if (checked)
                status = 1;
        } else if (id == R.id.radio_button_status_2) {
            if (checked)
                status = 2;
        }
    }
}