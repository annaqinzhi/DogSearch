package com.example.dogsearch.view.adapters_notused;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.dogsearch.R;

import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

public class DogAdapter extends RecyclerView.Adapter<DogAdapter.ViewHolder> {

    private List<String> images;
    private final RequestManager glide;

    public DogAdapter(List<String> images, RequestManager glide) {
        this.images = images;
        this.glide = glide;
    }

    @Override
    public DogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(DogAdapter.ViewHolder holder, int position) {
        String imageUrl = images.get(position);
        holder.textView.setText(imageUrl);
        glide.load(imageUrl).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public ViewHolder(final View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            textView = (TextView) itemView.findViewById(R.id.breed);

        }
    }
}
