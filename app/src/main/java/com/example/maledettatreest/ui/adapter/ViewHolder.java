package com.example.maledettatreest.ui.adapter;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.maledettatreest.R;
import com.example.maledettatreest.models.Line;

public class ViewHolder extends RecyclerView.ViewHolder{
    private final TextView line_start;
    private final TextView line_finish;
    public final Button direzione_1;
    public final Button direzione_2;
    public final Button map;

    public ViewHolder(View itemView) {
        super(itemView);
        //Log.d("ViewHolder", "Constructor");
        line_start = itemView.findViewById(R.id.tratta_1);
        line_finish = itemView.findViewById(R.id.tratta_2);
        direzione_1 = itemView.findViewById(R.id.direzione_1);
        direzione_2 = itemView.findViewById(R.id.direzione_2);
        map = itemView.findViewById(R.id.map);
    }

    public void setText(Line s) {
        //Log.d("ViewHolder", "setText");
        line_start.setText(s.terminus1.sname);
        line_finish.setText(s.terminus2.sname);
        direzione_1.setText(s.terminus1.sname);
        direzione_2.setText(s.terminus2.sname);
    }

}