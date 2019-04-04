package com.example.dogsearch.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dogsearch.DatabaseHandler;
import com.example.dogsearch.R;
import com.example.dogsearch.model.Dog;

import java.io.ByteArrayOutputStream;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DogAdapterGridView_Dog extends android.widget.BaseAdapter {

    private List<Dog> dogs;
    private int resource;
    private Context context;
    private DatabaseHandler db;

    public DogAdapterGridView_Dog(Context context, int resource, List<Dog> dogs) {

        this.context = context;
        this.dogs = dogs;
        this.resource = resource;
    }

    @Override
    public int getCount() {

        int nDogs = 0;
        if (dogs != null)
            nDogs = dogs.size();

        return nDogs;
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
            holder.textView = (TextView) view.findViewById(R.id.discp);
            holder.imageView = (ImageView) view.findViewById(R.id.image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Dog dog = dogs.get(position);

        if (isNetworkAvailable() ){
            Glide.with(context).load(dog.getImgLink()).into(holder.imageView);
        } else {
            holder.imageView.setImageBitmap(getBitmap(dog.getImg()));
        }

        if(!dog.getBreed().equals("show all") && !dog.getSubBreed().equals("show all")){
            holder.textView.setText("Breed is " + dog.getBreed() + " " + dog.getSubBreed());
        }

        return view;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dogs.get(position);
    }

    public Bitmap getBitmap(byte[] bitmap){
        return BitmapFactory.decodeByteArray(bitmap,0, bitmap.length);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork !=null && activeNetwork.isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
