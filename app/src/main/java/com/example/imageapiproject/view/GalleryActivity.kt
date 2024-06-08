package com.example.imageapiproject.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.imageapiproject.view.adapters.GalleryAdapter
import com.example.imageapiproject.model.ImageResult
import com.example.imageapiproject.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        val shareButton:ImageButton= findViewById(R.id.btn_share)
        val images: List<ImageResult> = intent.getParcelableArrayListExtra("images") ?: emptyList()
        val initialPosition: Int = intent.getIntExtra("position", 0)

        val galleryAdapter = GalleryAdapter(images)
        viewPager.adapter = galleryAdapter
        viewPager.setCurrentItem(initialPosition, false)
        shareButton.setOnClickListener {
            shareImage(images[viewPager.currentItem].webformatURL)
        }


    }

    private fun shareImage(imageUrl: String) {
        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val imageUri = saveImageToCache(resource)
                    if (imageUri != null) {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, getString(R.string.check_out_this_image))
                            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shared_image))
                            putExtra(Intent.EXTRA_STREAM, imageUri)
                            type = getString(R.string.image)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        startActivity(Intent.createChooser(shareIntent,
                            getString(R.string.share_image)))
                    } else {
                        Toast.makeText(this@GalleryActivity,
                            getString(R.string.failed_to_share_image), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle the placeholder
                }
            })
    }

    private fun saveImageToCache(bitmap: Bitmap): Uri? {
        return try {
            val cachePath = File(applicationContext.cacheDir, getString(R.string.images))
            cachePath.mkdirs()
            val file = File(cachePath, getString(R.string.shared_image_png))
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
            FileProvider.getUriForFile(applicationContext,
                getString(R.string.provider, applicationContext.packageName), file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}