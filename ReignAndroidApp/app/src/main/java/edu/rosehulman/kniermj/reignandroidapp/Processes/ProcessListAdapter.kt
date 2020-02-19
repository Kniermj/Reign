package edu.rosehulman.kniermj.reignandroidapp.Processes

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import edu.rosehulman.kniermj.reignandroidapp.ComandQueue.CommandQueueItem
import edu.rosehulman.kniermj.reignandroidapp.Constants
import edu.rosehulman.kniermj.reignandroidapp.R

class ProcessListAdapter(var sysId: String, var context: Context): RecyclerView.Adapter<ProcessHolder>() {

    var processList = ArrayList<ProcessItem>()
    private val processRef = FirebaseFirestore.getInstance()
        .collection("systems")
        .document(sysId)
        .collection("Processes")

    private val cmdRef = FirebaseFirestore.getInstance()
        .collection("systems")
        .document(sysId)
        .collection("CommandQueue")

    private val sysRef = FirebaseFirestore.getInstance()
        .collection("systems")
        .document(sysId)

    init{
        startListener()
    }

    private fun startListener() {
        processRef
            .orderBy("processName", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if(exception != null){
                    Log.d(Constants.TAG, exception.toString())
                    return@addSnapshotListener
                }
                for (documentChange in snapshot?.documentChanges!!) {
                    val processItem = ProcessItem.fromSnapshot(documentChange.document)
                    when (documentChange.type) {
                        DocumentChange.Type.ADDED -> {
                            var index = processList.indexOfLast{ it.processName < processItem.processName } + 1
                            processList.add(index, processItem)
                            notifyItemInserted(index)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            val index = processList.indexOfFirst { it.id == processItem.id }
                            processList[index] = processItem
                            notifyItemChanged(index)
                        }
                    }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProcessHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.process_card_view, parent, false)
        return ProcessHolder(view, this)
    }

    override fun getItemCount(): Int {
        return processList.size
    }

    override fun onBindViewHolder(holder: ProcessHolder, position: Int) {
        holder.bind(processList.get(position))
    }

    fun refreshProcesses() {
        processList.clear()
        notifyDataSetChanged()
        sysRef.update("refreshProcesses", true)
    }

    fun startProcessKillDialog(position: Int) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(context)
        builder.setTitle("Kill This Process")
        builder.setMessage("Name: ${processList.get(position).processName}")
        builder.setIcon(android.R.drawable.ic_input_delete)

        val killCommand = "Taskkill /PID ${processList.get(position).processId} /F"

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val command = CommandQueueItem(killCommand, "")
            cmdRef.add(command)
        }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.show()
    }
}