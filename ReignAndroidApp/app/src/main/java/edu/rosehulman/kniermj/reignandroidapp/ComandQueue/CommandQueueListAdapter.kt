package edu.rosehulman.kniermj.reignandroidapp.ComandQueue

import android.content.Context
import android.provider.SyncStateContract
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import edu.rosehulman.kniermj.reignandroidapp.Constants
import edu.rosehulman.kniermj.reignandroidapp.R
import edu.rosehulman.kniermj.reignandroidapp.SystemList.ComputerSystem
import edu.rosehulman.kniermj.reignandroidapp.SystemList.SystemViewHolder

class CommandQueueListAdapter(var sysId: String, var context: Context): RecyclerView.Adapter<CommandQueueHolder>() {

    var commandQueue = ArrayList<CommandQueueItem>()
    private val queueRef = FirebaseFirestore.getInstance()
        .collection("systems")
        .document(sysId)
        .collection("CommandQueue")

    init{
        startListener()
    }

    private fun startListener() {
        queueRef.orderBy("creation", Query.Direction.ASCENDING).addSnapshotListener { snapshot, exception ->
            if(exception != null){
                Log.d(Constants.TAG, exception.toString())
                return@addSnapshotListener
            }
            for (documentChange in snapshot?.documentChanges!!) {
                val queueItem = CommandQueueItem.fromSnapshot(documentChange.document)
                when (documentChange.type) {
                    DocumentChange.Type.ADDED -> {
                        commandQueue.add(commandQueue.size, queueItem)
                        notifyItemInserted(commandQueue.size - 1)
                    }
                    DocumentChange.Type.REMOVED -> {
                        val index = commandQueue.indexOfFirst { it.id == queueItem.id }
                        commandQueue.removeAt(index)
                        notifyItemRemoved(index)
                    }
                    DocumentChange.Type.MODIFIED -> {
                        val index = commandQueue.indexOfFirst { it.id == queueItem.id }
                        commandQueue[index] = queueItem
                        notifyItemChanged(index)
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: CommandQueueHolder, position: Int) {
        holder.bind(commandQueue.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommandQueueHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.command_queue_card_view, parent, false)
        return CommandQueueHolder(view, this)
    }

    override fun getItemCount(): Int {
        return commandQueue.size
    }

    public fun addCommandToQueue(command: CommandQueueItem){
        queueRef.add(command)
    }

}