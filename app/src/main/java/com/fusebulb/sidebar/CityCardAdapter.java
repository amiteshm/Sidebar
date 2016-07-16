package com.fusebulb.sidebar;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import java.util.List;

/**
 * Created by amiteshmaheshwari on 14/07/16.
 */
public class CityCardAdapter extends RecyclerView.Adapter<CityCardAdapter.ViewHolder> {

    private Context context;
    private List<City> cityList;

    public CityCardAdapter(Context context, List<City> cityList)
    {
        this.context = context;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        City city = cityList.get(position);
        holder.titleView.setText(city.getName());
        holder.thumbnailImage.setImageURI(Uri.fromFile(city.getPicture()));
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnailImage;
        public TextView titleView;
        public ViewHolder(View view) {
            super(view);
            thumbnailImage = (ImageView)view.findViewById(R.id.city_img_thumbnail);
            titleView = (TextView)view.findViewById(R.id.city_title);
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    City city = cityList.get(pos);
                    Intent intent = new Intent(context, TourListActivity.class);
                    intent.putExtra("TOURS_SOURCE", city.getCityTours());
                    context.startActivity(intent);
                    Toast.makeText(itemView.getContext(), "Position= "+ city.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
