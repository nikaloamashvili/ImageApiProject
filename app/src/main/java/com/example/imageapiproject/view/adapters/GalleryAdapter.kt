package com.example.imageapiproject.view.adapters

import android.content.ContentValues.TAG
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.imageapiproject.model.ImageResult
import com.example.imageapiproject.R
import com.bumptech.glide.request.target.Target


class GalleryAdapter(private val images: List<ImageResult>) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val image = images[position]
        Glide.with(holder.imageView.context)
            .load(image.webformatURL)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    // Hide progress bar if loading failed
                    holder.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    // Hide progress bar if image loaded successfully
                    holder.progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = images.size

    class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.full_image_view)
        val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)

    }
}
