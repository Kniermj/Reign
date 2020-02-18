package edu.rosehulman.kniermj.reignandroidapp.ComandQueue

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import edu.rosehulman.kniermj.reignandroidapp.Constants
import edu.rosehulman.kniermj.reignandroidapp.R
import edu.rosehulman.kniermj.reignandroidapp.Utlis.SwipeToDelete
import kotlinx.android.synthetic.main.dialog_add_command.view.*


class CommandQueueListAdapter(var sysId: String, var context: Context, var view: View):
    RecyclerView.Adapter<CommandQueueHolder>(),
    SwipeToDelete{

    var commandQueue = ArrayList<CommandQueueItem>()
    private val queueRef = FirebaseFirestore.getInstance()
        .collection("systems")
        .document(sysId)
        .collection("CommandQueue")

    init{
        startListener()
    }

    private fun startListener() {
        queueRef.orderBy("order", Query.Direction.ASCENDING).addSnapshotListener { snapshot, exception ->
            if(exception != null){
                Log.d(Constants.TAG, exception.toString())
                return@addSnapshotListener
            }
            for (documentChange in snapshot?.documentChanges!!) {
                val queueItem = CommandQueueItem.fromSnapshot(documentChange.document)
                when (documentChange.type) {
                    DocumentChange.Type.ADDED -> {
                        var index = commandQueue.indexOfLast{ it.order < queueItem.order } + 1
                        commandQueue.add(index, queueItem)
                        notifyItemInserted(index)
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
        val view = LayoutInflater.from(context).inflate(edu.rosehulman.kniermj.reignandroidapp.R.layout.command_queue_card_view, parent, false)
        return CommandQueueHolder(view, this)
    }

    override fun getItemCount(): Int {
        return commandQueue.size
    }


    override fun remove(position: Int) {
        val savedItem = commandQueue[position].copy()
        queueRef.document(commandQueue[position].id).delete()

        val snackbar = Snackbar
            .make(view, R.string.deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo, {
                queueRef.add(savedItem)
            })

        snackbar.show()

    }

    fun moveItemUp(pos: Int){
        Log.d(Constants.TAG, "moving item up")
        if(pos > 0){
            val temp = commandQueue[pos]
            commandQueue[pos] = commandQueue[pos - 1]
            commandQueue[pos -1] = temp;

            val tempOrder = commandQueue[pos].order
            commandQueue[pos].order = commandQueue[pos -1].order
            commandQueue[pos -1].order = tempOrder

            Log.d(Constants.TAG, "${commandQueue[pos].commandInput}: ${commandQueue[pos].order} ++" +
                    " ${commandQueue[pos-1].commandInput}: ${commandQueue[pos-1].order}")

            notifyItemMoved(pos,  pos - 1)
            queueRef.document(commandQueue[pos].id).update("order", commandQueue[pos].order)
            queueRef.document(commandQueue[pos-1].id).update("order", commandQueue[pos-1].order)
        }
    }

    fun moveItemDown(pos: Int){
        Log.d(Constants.TAG, "moving item up")
        if(pos < commandQueue.size - 1){
            val temp = commandQueue[pos]
            commandQueue[pos] = commandQueue[pos + 1]
            commandQueue[pos + 1] = temp;

            val tempOrder = commandQueue[pos].order
            commandQueue[pos].order = commandQueue[pos + 1].order
            commandQueue[pos + 1].order = tempOrder

            Log.d(Constants.TAG, "${commandQueue[pos].commandInput}: ${commandQueue[pos].order} ++" +
                    " ${commandQueue[pos + 1].commandInput}: ${commandQueue[pos + 1].order}")

            notifyItemMoved(pos,  pos + 1)
            queueRef.document(commandQueue[pos].id).update("order", commandQueue[pos].order)
            queueRef.document(commandQueue[pos + 1].id).update("order", commandQueue[pos + 1].order)
        }
    }


    public fun addCommandToQueue(command: CommandQueueItem){
        queueRef.add(command)
    }

    public fun editCommand(pos: Int) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(context!!)
        builder.setTitle("Update a command")
        val view = LayoutInflater.from(context).inflate(
            edu.rosehulman.kniermj.reignandroidapp.R.layout.dialog_add_command, null, false
        )
        view.command_entry.setText(commandQueue[pos].commandInput)
        builder.setView(view)
        builder.setIcon(android.R.drawable.ic_input_add)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            commandQueue[pos].commandInput = view.command_entry.text.toString()
            queueRef.document(commandQueue[pos].id).set(commandQueue[pos])

        }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.show()

    }

}