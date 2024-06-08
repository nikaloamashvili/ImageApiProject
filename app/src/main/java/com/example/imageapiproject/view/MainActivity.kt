package com.example.imageapiproject.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.imageapiproject.R
import com.example.imageapiproject.model.ImageResult
import com.example.imageapiproject.view.adapters.ImageAdapter
import com.example.imageapiproject.viewmodel.MainViewModel
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.JustifyContent

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var imageAdapter: ImageAdapter
    private val images = mutableListOf<ImageResult>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        setContentView(com.example.imageapiproject.R.layout.activity_main)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        val padding = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        val numRows = 6
        val fixedHeight = (screenHeight - (padding * (numRows + 1))) / numRows


        val toolbar: Toolbar = findViewById(com.example.imageapiproject.R.id.toolbar)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val searchEditText: EditText = findViewById(com.example.imageapiproject.R.id.search_edit_text)
        val searchButton: Button = findViewById(com.example.imageapiproject.R.id.search_button)
        val imageRecyclerView: RecyclerView = findViewById(com.example.imageapiproject.R.id.image_recycler_view)

        imageAdapter = ImageAdapter(images)
        imageAdapter.setFixedHeight(fixedHeight)

        imageRecyclerView.adapter = imageAdapter
        val flexboxLayoutManager = FlexboxLayoutManager(this)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        imageRecyclerView.layoutManager = flexboxLayoutManager
        imageRecyclerView.addItemDecoration(FlexboxItemDecoration(5))

        viewModel.images.observe(this) { newImages ->
            images.clear()
            images.addAll(newImages)
            imageAdapter.notifyDataSetChanged()

            if (newImages.isEmpty()) {
                Toast.makeText(this, getString(R.string.no_images_found), Toast.LENGTH_SHORT).show()
            }
        }

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            viewModel.searchImages(query)
            closeKeyboard()
        }

        imageRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.loadMoreImages()
                }
            }
        })

        searchEditText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    // Perform action on key press
                    val query = searchEditText.text.toString()
                    viewModel.searchImages(query)
                    closeKeyboard()
                    return true
                }
                return false
            }
        })
    }

    private fun closeKeyboard() {
        // this will give us the view
        // which is currently focus
        // in this layout
        val view = this.currentFocus

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            val manager = getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager
            manager
                .hideSoftInputFromWindow(
                    view.windowToken, 0
                )
        }
    }
}
