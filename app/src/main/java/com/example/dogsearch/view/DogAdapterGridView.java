package com.example.dogsearch.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dogsearch.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DogAdapterGridView extends android.widget.BaseAdapter {

    private List<String> images;
    private int resource;
    private Context context;

    public DogAdapterGridView(Context context, int resource, List<String> images) {

        this.context = context;
        this.images = images;
        this.resource = resource;
    }

    @Override
    public int getCount() {

        int nImages = 0;
        if (images != null)
            nImages = images.size();

        return nImages;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    class ViewHolder {

        ImageView imageView;
        TextView textView;


    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;
        if (view == null) {

            view = LayoutInflater.from(context).inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) view.findViewById(R.id.breed);
            holder.imageView = (ImageView) view.findViewById(R.id.image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String imageUrl = images.get(position);

        Glide.with(context).load(imageUrl).into(holder.imageView);
        //holder.textView.setText(imageUrl);

        return view;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

}
