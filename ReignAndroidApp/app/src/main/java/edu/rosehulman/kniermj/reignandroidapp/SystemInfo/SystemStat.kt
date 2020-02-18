package edu.rosehulman.kniermj.reignandroidapp.SystemInfo

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import edu.rosehulman.kniermj.reignandroidapp.SystemList.ComputerSystem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SystemStat(
    var cpuUsage: Float = 0f,
    var memUsage: Float = 0f,
    var memFree: Float = 0f

) : Parcelable {

    @get:Exclude
    var id = ""
    @ServerTimestamp
    var creation: Timestamp? = null

    companion object {
        fun fromSnapshot(snapshot: DocumentSnapshot): SystemStat {
            val loadedStat = snapshot.toObject(SystemStat::class.java)!!
            loadedStat.id = snapshot.id
            return loadedStat
        }
    }

}