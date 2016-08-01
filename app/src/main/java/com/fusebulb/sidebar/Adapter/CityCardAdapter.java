package com.fusebulb.sidebar.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.fusebulb.sidebar.City;
import com.fusebulb.sidebar.R;
import com.fusebulb.sidebar.TourListActivity;
import com.fusebulb.sidebar.UserSettings;

import java.util.List;


/**
 * Created by amiteshmaheshwari on 14/07/16.
 */
public class CityCardAdapter extends RecyclerView.Adapter<CityCardAdapter.ViewHolder>  {

    private Context context;
    private List<City> cityList;
    private String languagePref;

    public CityCardAdapter(Context context, String languagePref, List<City> cityList)
    {
        this.context = context;
        this.languagePref = languagePref;
        this.cityList = cityList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        City city = cityList.get(position);
        holder.titleView.setText(city.getName());
        holder.buttonTag.setText(getTourInfoTag(city, context));
        holder.thumbnailImage.setImageURI(Uri.fromFile(city.getPicture()));

        holder.buttonTag.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showToursLink(position);
            }
        });
    }

    public String getTourInfoTag(City city, Context context){
        Resources rs = context.getResources();
        int size = city.getTourSize();
        String tag = "";
        if(languagePref.equals(UserSettings.lang_hi)){
            if(size == 0){
                tag = rs.getString(R.string.hi_coming_soon);
            }else{
                tag = rs.getQuantityString(R.plurals.hi_city_tour_tag, size, size);
            }
        } else {
            if(size == 0){
                tag = rs.getString(R.string.en_coming_soon);
            }else{
                tag = rs.getQuantityString(R.plurals.en_city_tour_tag, size, size);
            }
        }

        return tag;
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnailImage;
        public TextView titleView;
        public Button buttonTag;
        public ViewHolder(View view) {
            super(view);
            thumbnailImage = (ImageView)view.findViewById(R.id.city_img_thumbnail);
            titleView = (TextView)view.findViewById(R.id.city_title);
            buttonTag = (Button)view.findViewById(R.id.city_info_btn);
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    showToursLink(pos);

                }
            });
        }
    }

    private void showToursLink(int position){
        City city = cityList.get(position);
        if(city.getTourSize() != 0){
            Intent intent = new Intent(context, TourListActivity.class);
            intent.putExtra("TOURS_SOURCE", city.getCityTours());
            context.startActivity(intent);
        } else{
            Toast.makeText(context, getTourInfoTag(city, context), Toast.LENGTH_LONG).show();
        }
    }
}
