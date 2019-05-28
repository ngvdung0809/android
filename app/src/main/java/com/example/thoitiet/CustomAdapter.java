package com.example.thoitiet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<ThoiTiet> arrayList;

    public CustomAdapter(Context context, ArrayList<ThoiTiet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.dong_listview,null);

        ThoiTiet thoiTiet=arrayList.get(position);

        TextView txtDay= convertView.findViewById(R.id.day);
        TextView txtStatus= convertView.findViewById(R.id.status);
        TextView txtTemp= convertView.findViewById(R.id.temp);
        ImageView imgicon=convertView.findViewById(R.id.icon) ;
        TextView txtdayfm=convertView.findViewById(R.id.dayformat);


        txtDay.setText(thoiTiet.day);
        txtStatus.setText(thoiTiet.status);
        txtTemp.setText(thoiTiet.temp);
        txtdayfm.setText(thoiTiet.dayformat);

        Integer a=thoiTiet.icon;
        String icon;
        if(a<10){
            icon="0"+a.toString();
        }
        else{
            icon=thoiTiet.icon.toString();
        }
        Picasso.with(context).load("https://developer.accuweather.com/sites/default/files/"+icon+"-s.png").into(imgicon);
        return convertView;
    }
}
