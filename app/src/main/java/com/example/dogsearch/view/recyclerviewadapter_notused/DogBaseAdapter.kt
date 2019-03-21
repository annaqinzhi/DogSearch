package com.example.dogsearch.view.recyclerviewadapter_notused

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.example.dogsearch.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager

class DogBaseAdapter(private val images: List<String>, private val glide: RequestManager) : RecyclerView.Adapter<DogBaseAdapter.MyViewHolder>() {

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textView: TextView
        val image: ImageView

        init {
            textView = v.findViewById(R.id.breed)
            image = v.findViewById(R.id.image)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {

        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_view, viewGroup, false)

        return MyViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        viewHolder.textView.text = images[position]
        glide.load(images[position]).into(viewHolder.image)
    }

    override fun getItemCount() = images.size

}