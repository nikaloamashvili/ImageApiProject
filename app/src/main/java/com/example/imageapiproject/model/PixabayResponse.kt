package com.example.imageapiproject.model

import com.example.imageapiproject.model.ImageResult

data class PixabayResponse(
    val total: Int,
    val totalHits: Int,
    val hits: List<ImageResult>
)