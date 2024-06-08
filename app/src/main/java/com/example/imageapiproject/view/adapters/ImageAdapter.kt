package com.example.imageapiproject.view.adapters

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.imageapiproject.model.ImageResult
import com.example.imageapiproject.R
import com.example.imageapiproject.view.GalleryActivity
import com.bumptech.glide.request.target.Target


class ImageAdapter(private val images: List<ImageResult>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private var fixedHeight: Int = 0

    fun setFixedHeight(height: Int) {
        fixedHeight = height
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_view)
        val imageContainer:LinearLayout= view.findViewById(R.id.image_container)
        val progressBar:ProgressBar =view.findViewById(R.id.progress_bar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        val layoutParams = holder.imageView.layoutParams
        holder.imageContainer.layoutParams.height=fixedHeight
        // Assuming the parent LinearLayout height is fixed to 200dp, converting to pixels
        val context = holder.itemView.context
//        val fixedHeight = context.resources.getDimensionPixelSize(R.dimen.fixed_height)

        val aspectRatio = image.previewWidth.toFloat() / image.previewHeight.toFloat()
        layoutParams.height = fixedHeight
        layoutParams.width = ((fixedHeight) * aspectRatio).toInt() // Maintain aspect ratio
        holder.imageView.layoutParams = layoutParams

        holder.progressBar.visibility = View.VISIBLE

        Glide.with(holder.itemView.context)
            .load(image.previewURL)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Hide progress bar if loading failed
                    holder.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Hide progress bar if image loaded successfully
                    holder.progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(holder.imageView)

        holder.imageView.setOnClickListener {
            val context = holder.imageView.context
            val intent = Intent(context, GalleryActivity::class.java).apply {
                putParcelableArrayListExtra(context.getString(R.string.images), ArrayList(images))
                putExtra(context.getString(R.string.position), position)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = images.size
}
