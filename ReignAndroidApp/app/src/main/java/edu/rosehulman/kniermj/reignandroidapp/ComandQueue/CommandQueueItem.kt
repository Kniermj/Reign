package edu.rosehulman.kniermj.reignandroidapp.ComandQueue

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import edu.rosehulman.kniermj.reignandroidapp.SystemList.ComputerSystem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommandQueueItem(
    var commandInput: String = "",
    var commandOutput: String = ""
) : Parcelable {
    @get:Exclude
    var id = ""
    @ServerTimestamp
    var creation: Timestamp? = null

    companion object {
        fun fromSnapshot(snapshot: DocumentSnapshot): CommandQueueItem {
            val loadedCommand = snapshot.toObject(CommandQueueItem::class.java)!!
            loadedCommand.id = snapshot.id
            return loadedCommand
        }
    }
}