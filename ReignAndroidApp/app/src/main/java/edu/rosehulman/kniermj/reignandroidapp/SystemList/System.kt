package edu.rosehulman.kniermj.reignandroidapp.SystemList

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class System(
    var name: String = "",
    var active: String = "",
    var usage: String = ""
) : Parcelable {

}