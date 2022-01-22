package com.example.maledettatreest.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

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

    public static Bitmap fromBase64ToBitmap(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static void showErrorNetwork(Context context) {
        new MaterialAlertDialogBuilder(context)
                .setTitle("Errore")
                .setMessage("Non c'è una connessione di rete disponibile")
                .setPositiveButton("Chiudi", null).show();
    }

    public static void calculateMedia(String date, int dalay, Context context) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = dateFormat.parse(date);
            dateFormat = new SimpleDateFormat("EEEE");
            String d = dateFormat.format(date1);

            SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE);
            if (!sharedpreferences.contains(d)) {
                sharedpreferences.edit().putInt(d, dalay).apply();
            } else {
                int c = sharedpreferences.getInt(d, 0);
                c = (c + dalay) / 2;
                sharedpreferences.edit().putInt(d, c).apply();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static String getMedia(Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE);
        if (!sharedpreferences.contains(dayOfTheWeek)) {
            return "Viene calcolata mentre utilizzi la app";
        } else {
            int c = sharedpreferences.getInt(dayOfTheWeek, 0);
            return c + " minuti";
        }
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

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
