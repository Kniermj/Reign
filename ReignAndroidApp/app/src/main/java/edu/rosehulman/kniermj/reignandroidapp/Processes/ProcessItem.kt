package edu.rosehulman.kniermj.reignandroidapp.Processes

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import edu.rosehulman.kniermj.reignandroidapp.ComandQueue.CommandQueueItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProcessItem(
    var processId: String = "",
    var processName: String = ""
) : Parcelable {
    @get:Exclude
    var id = ""

    companion object {
        fun fromSnapshot(snapshot: DocumentSnapshot): ProcessItem {
            val loadedProcess = snapshot.toObject(ProcessItem::class.java)!!
            loadedProcess.id = snapshot.id
            return loadedProcess
        }
    }
}