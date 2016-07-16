package com.fusebulb.sidebar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by amiteshmaheshwari on 15/07/16.
 */

public class TourCardAdapter extends RecyclerView.Adapter<TourCardAdapter.ViewHolder>{

    private Context context;
    private List<Tour> tourList;

    public TourCardAdapter(Context context, List<Tour> tourList)
    {
        this.context = context;
        this.tourList = tourList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tour_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tour tour = tourList.get(position);
        holder.titleView.setText(tour.getName());
        holder.thumbnailImage.setImageURI(Uri.fromFile(tour.getPicture()));
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnailImage;
        public TextView titleView;
        public ViewHolder(View view) {
            super(view);
            thumbnailImage = (ImageView)view.findViewById(R.id.tour_img_thumbnail);
            titleView = (TextView)view.findViewById(R.id.tour_title);
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Tour tour= tourList.get(pos);
//                    Intent intent = new Intent(context, TourListActivity.class);
//                    intent.putExtra("TOURS_SOURCE", city.getCityTours());
//                    context.startActivity(intent);
                    Toast.makeText(itemView.getContext(), "Position= "+ tour.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
