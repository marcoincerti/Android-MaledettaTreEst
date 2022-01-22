package com.example.maledettatreest.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.contentcapture.ContentCaptureCondition;

import com.example.maledettatreest.R;
import com.example.maledettatreest.models.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String getSessionID(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE);
        String a = null;
        if (sharedpreferences.contains(context.getString(R.string.session_id))) {
            sharedpreferences.getString("sid", a);
            return sharedpreferences.getString(context.getString(R.string.session_id), null);
        } else
            return "-1";
    }

    public static void saveSessionID(Context context, String sid) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE);
        sharedpreferences.edit().putString("sid", sid).apply();
    }

    public static boolean checkFollower(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE);
        if (!sharedpreferences.contains("seguo"))
            sharedpreferences.edit().putInt("seguo", 0).apply();
        return true;
    }

    public static String getFollowers(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE);
        if (checkFollower(context)) {
            return String.valueOf(sharedpreferences.getInt("seguo", 0));
        } else
            return "-1";
    }

    public static void addFollowers(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE);
        if (checkFollower(context)) {
            int c = sharedpreferences.getInt("seguo", 0);
            c = c + 1;
            sharedpreferences.edit().putInt("seguo", c).apply();
        }
    }

    public static void deleteFollowers(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE);
        if (checkFollower(context)) {
            int c = sharedpreferences.getInt("seguo", 0);
            if (c != 0) {
                c = c - 1;
            }
            sharedpreferences.edit().putInt("seguo", c).apply();
        }
    }

    public static JSONObject getSessionIDJsonRequest(Context context) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        return jsonObject.put("sid", getSessionID(context));
    }

    public static JSONObject getStationJsonRequest(Context context, String did) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("did", did);
        return jsonObject.put("sid", getSessionID(context));
    }

    public static JSONObject getPictureJsonRequest(Context context, String uid) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uid", uid);
        return jsonObject.put("sid", getSessionID(context));
    }

    public static JSONObject setUserJsonRequest(Context context, User user) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sid", getSessionID(context));
        jsonObject.put("name", user.name);
        jsonObject.put("picture", user.picture);
        return jsonObject;
    }

    public static String fromBitmatToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);

    }

    public static Bitmap fromBase64ToBitmap(String base64, Context context) {
        try{
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }catch (Exception e){
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.placeholder);
        }
    }

    public static void showErrorNetwork(Context context) {
        new MaterialAlertDialogBuilder(context)
                .setTitle("Errore")
                .setMessage("Non c'è una connessione di rete disponibile")
                .setPositiveButton("Chiudi", null).show();
    }

    public static JSONObject addPost(Context context, String did, int delay, int status, String commento)  throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sid", getSessionID(context));
        jsonObject.put("did", did);
        jsonObject.put("delay", delay);
        jsonObject.put("status", status);
        jsonObject.put("comment", commento);
        return jsonObject;
    }

    public static String getBacheca(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE);
        if (sharedpreferences.contains("bacheca")) {
            return sharedpreferences.getString("bacheca", null);
        } else
            return "-1";
    }

    public static String getBachecaSname(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE);
        if (sharedpreferences.contains("bacheca_sname")) {
            return sharedpreferences.getString("bacheca_sname", null);
        } else
            return "-1";
    }
    public static String getBachecaDid_2(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE);
        if (sharedpreferences.contains("bacheca_did_2")) {
            return sharedpreferences.getString("bacheca_did_2", null);
        } else
            return "-1";
    }
    public static String getBachecaSname_2(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE);
        if (sharedpreferences.contains("bacheca_sname_2")) {
            return sharedpreferences.getString("bacheca_sname_2", null);
        } else
            return "-1";
    }

    public static void saveBacheca(Context context, String nBacheca, String sname, String did_2
            , String sname_2) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE);
        sharedpreferences.edit().putString("bacheca", nBacheca).apply();
        sharedpreferences.edit().putString("bacheca_sname", sname).apply();
        sharedpreferences.edit().putString("bacheca_did_2", did_2).apply();
        sharedpreferences.edit().putString("bacheca_sname_2", sname_2).apply();
    }

}
