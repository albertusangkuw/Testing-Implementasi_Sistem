package com.tubes.emusic.entity

import android.os.Parcel
import android.os.Parcelable

@Parcelize
data class Music (
        var idsong   : String?,
        var idalbum  : String?,
        var title    : String?,
        var urlAlbumPhoto : String?,
        var urlsongs : String?,
        var artistName : String?,
        var genre    : String?
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idsong)
        parcel.writeString(idalbum)
        parcel.writeString(title)
        parcel.writeString(urlsongs)
        parcel.writeString(genre)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Music> {
        override fun createFromParcel(parcel: Parcel): Music {
            return Music(parcel)
        }

        override fun newArray(size: Int): Array<Music?> {
            return arrayOfNulls(size)
        }
    }
}