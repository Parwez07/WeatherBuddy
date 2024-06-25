package com.example.weatherapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherapp.Model.HourlyData;
import com.example.weatherapp.Model.pair;
import com.example.weatherapp.R;

import java.util.ArrayList;
import java.util.List;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourlyViewModel> {

    List<pair> dataList = new ArrayList<>();
    Context context;
    public HourlyAdapter(Context context, List<pair> list){
        this.context = context;
        this.dataList = list;
    }

    @NonNull
    @Override
    public HourlyAdapter.HourlyViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_layout,parent,false);
        return new HourlyViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyAdapter.HourlyViewModel holder, int position) {
        pair data = dataList.get(position);
        Log.d("hourly","inside binding "+data.getTime()+" "+data.getTemp()+" "+data.getWeatherCode());
        holder.temps.setText(data.getTemp()+" °C");
        holder.hours.setText(data.getTime());

        int wc = data.getWeatherCode();

        if(wc==0){
            holder.pic.setImageResource(R.drawable.sunny);
        }
        else if (wc==1 || wc==2 || wc==3){
            holder.pic.setImageResource(R.drawable.cloudy_sunny);
        }
        else if(wc==45 || wc==48){
            holder.pic.setImageResource(R.drawable.cloudy);
        }
        else if(wc>50 && wc<60 ||wc==95 ||wc==96 || wc==99){
            holder.pic.setImageResource(R.drawable.storm);
        }
        else if(wc>60 && wc<70 || (wc>=80 && wc<=82)){
            holder.pic.setImageResource(R.drawable.rainy);
        }
        else if(wc>=70 && wc<80){
            holder.pic.setImageResource(R.drawable.snowy);
        }
        else{
            holder.pic.setImageResource(R.drawable.sunny);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class  HourlyViewModel extends RecyclerView.ViewHolder{

        TextView hours,temps;
        ImageView pic;
        public HourlyViewModel(@NonNull View itemView) {
            super(itemView);
            hours = itemView.findViewById(R.id.tvHour);
            temps = itemView.findViewById(R.id.tvTemp);
            pic = itemView.findViewById(R.id.ivPic);
        }
    }
}
