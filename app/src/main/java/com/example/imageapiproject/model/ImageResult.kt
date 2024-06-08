package com.example.imageapiproject.model

import android.os.Parcel
import android.os.Parcelable

data class ImageResult(
    val id: Int,
    val webformatURL: String,
    val imageWidth: Int,
    val imageHeight: Int,
    val previewURL: String,
    val previewWidth: Int,
    val previewHeight: Int,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt()
        )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(webformatURL)
        parcel.writeInt(imageWidth)
        parcel.writeInt(imageHeight)
        parcel.writeString(previewURL)
        parcel.writeInt(previewWidth)
        parcel.writeInt(previewHeight)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageResult> {
        override fun createFromParcel(parcel: Parcel): ImageResult {
            return ImageResult(parcel)
        }

        override fun newArray(size: Int): Array<ImageResult?> {
            return arrayOfNulls(size)
        }
    }
}
