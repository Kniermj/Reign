package edu.rosehulman.kniermj.reignandroidapp.SystemList

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ComputerSystem(
    var name: String = "",
    var active: Boolean = false,
    var refreshtime: Int = 0,
    var owner: String = ""
) : Parcelable {

    @get:Exclude
    var id = ""
    companion object {
        fun fromSnapshot(snapshot: DocumentSnapshot): ComputerSystem {
            val loadedSysystem = snapshot.toObject(ComputerSystem::class.java)!!
            loadedSysystem.id = snapshot.id
            return loadedSysystem
        }
    }

}