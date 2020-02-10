package edu.rosehulman.kniermj.reignandroidapp.HistoryQueue

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import edu.rosehulman.kniermj.reignandroidapp.ComandQueue.CommandQueueHolder
import edu.rosehulman.kniermj.reignandroidapp.ComandQueue.CommandQueueItem
import edu.rosehulman.kniermj.reignandroidapp.Constants
import edu.rosehulman.kniermj.reignandroidapp.R

class CommandHistoryListAdapter(var sysId: String, var context: Context): RecyclerView.Adapter<CommandHistoryHolder>() {

    var commandHistory = ArrayList<CommandQueueItem>()
    private val histRef = FirebaseFirestore.getInstance()
        .collection("systems")
        .document(sysId)
        .collection("CommandHistory")

    init{
        startListener()
    }

    private fun startListener() {
        histRef.addSnapshotListener { snapshot, exception ->
            if(exception != null){
                Log.d(Constants.TAG, exception.toString())
                return@addSnapshotListener
            }
            Log.d(Constants.TAG, "XXXXXXXXXXXXXXXXXXXXX${snapshot?.size()}")
            for (documentChange in snapshot?.documentChanges!!) {
                val historyItem = CommandQueueItem.fromSnapshot(documentChange.document)
                when (documentChange.type) {
                    DocumentChange.Type.ADDED -> {
                        commandHistory.add(0, historyItem)
                        notifyItemInserted(0)
                    }
                    DocumentChange.Type.REMOVED -> {
                        val index = commandHistory.indexOfFirst { it.id == historyItem.id }
                        commandHistory.removeAt(index)
                        notifyItemRemoved(index)
                    }
                    DocumentChange.Type.MODIFIED -> {
                        val index = commandHistory.indexOfFirst { it.id == historyItem.id }
                        commandHistory[index] = historyItem
                        notifyItemChanged(index)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommandHistoryHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.command_history_card_view, parent, false)
        return CommandHistoryHolder(view, this)
    }

    override fun getItemCount(): Int {
        return commandHistory.size
    }

    override fun onBindViewHolder(holder: CommandHistoryHolder, position: Int) {
        holder.bind(commandHistory.get(position))
    }
}