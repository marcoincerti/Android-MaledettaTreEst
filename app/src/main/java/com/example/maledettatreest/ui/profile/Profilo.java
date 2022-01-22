package com.example.maledettatreest.ui.profile;

import static com.github.razir.progressbutton.ProgressButtonHolderKt.bindProgressButton;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.maledettatreest.R;
import com.example.maledettatreest.databinding.FragmentProfiloBinding;
import com.example.maledettatreest.models.User;
import com.example.maledettatreest.network.VolleySingleton;
import com.example.maledettatreest.utils.Utils;
import com.github.razir.progressbutton.DrawableButton;
import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.github.razir.progressbutton.ProgressParams;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class Profilo extends Fragment {

    FragmentProfiloBinding binding;
    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

    ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    ActivityResultLauncher<Intent> galleryActivityResultLauncher;

    private User user;

    public Profilo() {
        // Required empty public constructor
    }

    public static Profilo newInstance() {
        return new Profilo();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        binding.imageView.setImageBitmap(createSquaredBitmap(imageBitmap));
                        //this.user.picture = Utils.fromBitmatToBase64(imageBitmap);
                        btn_Enable(true);
                    }

                });

        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Uri selectedImage = data.getData();
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        binding.imageView.setImageBitmap(createSquaredBitmap(bitmap));
                        //this.user.picture = Utils.fromBitmatToBase64(bitmap);
                        btn_Enable(true);
                    }

                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentProfiloBinding.inflate(inflater, container, false);

        try {
            getProfile();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bindProgressButton(this.getViewLifecycleOwner(), binding.buttonSave);
        binding.buttonSave.setTextColor(Color.WHITE);
        btn_Enable(false);

        binding.btnCamera.setOnClickListener(view -> {
            try {
                cameraActivityResultLauncher.launch(takePicture);
            } catch (ActivityNotFoundException e) {
                // display error state to the user
            }
        });

        binding.btnGallery.setOnClickListener(view -> {
            galleryActivityResultLauncher.launch(pickPhoto);
            //startActivityForResult(pickPhoto , 1);
        });

        binding.buttonSave.setOnClickListener(v -> {
            showProgressLeft(binding.buttonSave);
            try {
                user.name = binding.nameLayout.getEditText().getText().toString();
                setProfile();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        binding.btnElimina.setOnClickListener(v -> {
            loadProfile();
            btn_Enable(false);
        });

        binding.nameLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (binding.nameLayout.getEditText().getText().toString().equals("")) {
                    binding.nameLayout.setError("Non puoi lasciarlo in bianco");
                    btn_Enable(false);
                } else if (!binding.nameLayout.getEditText().getText().toString().equals(user.name)) {
                    btn_Enable(true);
                    binding.nameLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i("ciao", binding.nameLayout.getEditText().getText().toString());
                if (!binding.nameLayout.getEditText().getText().toString().equals(user.name))
                    btn_Enable(true);
            }
        });

        return binding.getRoot();
    }


    private void setProfile() throws JSONException {
        String url = "https://ewserver.di.unimi.it/mobicomp/treest/setProfile.php";
        binding.imageView.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) binding.imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        this.user.picture = Utils.fromBitmatToBase64(bitmap);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, Utils.setUserJsonRequest(this.getContext(), this.user),
                response -> {
                    Log.i("ciao", response.toString() + " ok");
                    DrawableButtonExtensionsKt.hideProgress(binding.buttonSave, R.string.saved);
                    btn_Enable(false);
                }, error -> {
            Utils.showErrorNetwork(this.getContext());
            Log.e("spash activity", "Errore volley");
            DrawableButtonExtensionsKt.hideProgress(binding.buttonSave, R.string.saved);
        });

        VolleySingleton.getInstance(this.getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void getProfile() throws JSONException {
        String url = "https://ewserver.di.unimi.it/mobicomp/treest/getProfile.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, Utils.getSessionIDJsonRequest(this.getContext()),
                response -> {
                    Gson gson = new Gson();
                    user = gson.fromJson(response.toString(), User.class);
                    loadProfile();
                    Log.i("ciao", response.toString() + " " + user);
                }, error -> {
            Utils.showErrorNetwork(this.getContext());
            Log.e("spash activity", "Errore volley");
        });

        VolleySingleton.getInstance(this.getContext()).addToRequestQueue(jsonObjectRequest);
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


    private void loadProfile() {
        binding.nameLayout.getEditText().setText(user.name);
        binding.textUid.setText(user.uid);
        binding.txtNPersone.setText(Utils.getFollowers(this.getContext()));
        if (this.user.picture != null)
            binding.imageView.setImageBitmap(Utils.fromBase64ToBitmap(this.user.picture, this.getContext()));
    }

    private void btn_Enable(boolean active) {
        binding.buttonSave.setEnabled(active);
        if (active) {
            binding.buttonSave.setBackgroundColor(getResources().getColor(R.color.shrine_pink_100));
            binding.buttonSave.setText(R.string.salva);
            binding.btnElimina.setBackgroundColor(Color.RED);
        } else {
            binding.buttonSave.setBackgroundColor(Color.LTGRAY);
            binding.btnElimina.setBackgroundColor(Color.LTGRAY);
        }
    }

    private static Bitmap createSquaredBitmap(Bitmap srcBmp) {
        int dim = Math.max(srcBmp.getWidth(), srcBmp.getHeight());
        Bitmap dstBmp = Bitmap.createBitmap(dim, dim, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(dstBmp);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(srcBmp, (dim - srcBmp.getWidth()) / 2, (dim - srcBmp.getHeight()) / 2, null);
        Log.i("ciao", String.valueOf(dstBmp.getWidth()));
        Log.i("ciao", String.valueOf(dstBmp.getHeight()));
        return dstBmp;
    }
}