package com.tubes.emusic.entity

import android.os.Parcel
import android.os.Parcelable

@Parcelize
data class Thumbnail (
        var id: String?,
        var type: String?, // Music,Album,Playlist,Artist,Regular
        var urlImage: String?,
        var title:String?,
        var description: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Thumbnail> {
        override fun createFromParcel(parcel: Parcel): Thumbnail {
            return Thumbnail(parcel)
        }

        override fun newArray(size: Int): Array<Thumbnail?> {
            return arrayOfNulls(size)
        }
    }
}

annotation class Parcelize
