package com.example.maledettatreest.models;

import android.content.Context;

import com.example.maledettatreest.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

//IMPLEMENTO IL MODEL CON IL PATTERN SINGLETON
public class ApplicationModel {
    private static ApplicationModel ourInstance = null;

    public static ArrayList<Line> lista_linee = new ArrayList<>();
    public static ArrayList<Post> lista_posts = new ArrayList<>();

    public static synchronized ApplicationModel getInstance() {
        if (ourInstance == null)
            ourInstance = new ApplicationModel();
        return ourInstance;
    }

    private ApplicationModel() {
    }


    public ArrayList<Line> getLines() {
        return lista_linee;
    }
    public ArrayList<Post> getPost() {
        return lista_posts;
    }


    //metodi per l'adapter
    public Line getTratta(int index) {
        return ApplicationModel.getInstance().getLines().get(index);
    }

    public Post getPost(int index) {
        return ApplicationModel.getInstance().getPost().get(index);
    }

    public int getSize() {
        return ApplicationModel.getInstance().getLines().size();
    }
    public int getSizePost() {
        return ApplicationModel.getInstance().getPost().size();
    }

    public void initFromJson(JSONObject networkResponse) {
        System.out.println("Ricevo oggetto Json: " + networkResponse.toString());
        lista_linee.clear();
        try {
            JSONArray json = networkResponse.getJSONArray("lines");

            int length = json.length();

            for (int i = 0; i < length; i++) {

                JSONObject terminus_1 = json.getJSONObject(i).getJSONObject("terminus1");
                JSONObject terminus_2 = json.getJSONObject(i).getJSONObject("terminus2");

                Terminus terminus1 = new Terminus(terminus_1.getString("sname"), terminus_1.getString("did"));
                Terminus terminus2 = new Terminus(terminus_2.getString("sname"), terminus_2.getString("did"));

                Line line = new Line(terminus1, terminus2);

                lista_linee.add(line);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initPostFromJson(JSONObject networkResponse, Context context) {
        System.out.println("Ricevo oggetto Json: " + networkResponse.toString());
        lista_posts.clear();
        try {
            JSONArray json = networkResponse.getJSONArray("posts");

            int length = json.length();

            for (int i = 0; i < length; i++) {

                Gson gson = new Gson();
                Post post = gson.fromJson(json.getJSONObject(i).toString(), Post.class);
                if(post.delay == null){
                    post.delay = "0";
                }
                Utils.calculateMedia(post.datetime, Integer.parseInt(post.delay), context);

                lista_posts.add(post);
            }
            //Collections.reverse(lista_posts);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
