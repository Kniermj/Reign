package edu.rosehulman.kniermj.reignandroidapp.SystemList

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SystemRequest(
    val systemID: String = "",
    val userID: String = "",
    val systemToken: String = ""
) : Parcelable {
    @get:Exclude
    var id = ""
}