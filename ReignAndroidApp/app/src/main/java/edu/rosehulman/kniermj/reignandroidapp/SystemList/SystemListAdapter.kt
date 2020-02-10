package edu.rosehulman.kniermj.reignandroidapp.SystemList

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.kniermj.reignandroidapp.Constants
import edu.rosehulman.kniermj.reignandroidapp.R

class SystemListAdapter(
    var context: Context,
    var uid: String,
    val listener: OnSystemSelectlistener?):
    RecyclerView.Adapter<SystemViewHolder>() {

    private var systemList = ArrayList<ComputerSystem>()
    private val requestRef = FirebaseFirestore.getInstance()
        .collection("access_requests")
    private val sysRef = FirebaseFirestore.getInstance()
        .collection(Constants.SYSTEMS_REF_NAME)

    init{
        startListeners()
    }

    private fun startListeners() {
        Log.d(Constants.TAG, "Listener for systems user: ${uid}")
        sysRef.whereEqualTo("owner", uid).addSnapshotListener { querySnapshot, e ->
            if(e != null){
                Log.d(Constants.TAG, e.toString())
                return@addSnapshotListener
            }
            for (documentChange in querySnapshot?.documentChanges!!) {
                val systemItem = ComputerSystem.fromSnapshot(documentChange.document)
                when (documentChange.type) {
                    DocumentChange.Type.ADDED -> {
                        Log.d(Constants.TAG, "system added to 0")
                        systemList.add(0, systemItem)
                        notifyItemInserted(0)
                    }
                    DocumentChange.Type.REMOVED -> {
                        val index = systemList.indexOfFirst { it.id == systemItem.id }
                        Log.d(Constants.TAG, "system removed from ${index}")
                        systemList.removeAt(index)
                        notifyItemRemoved(index)
                    }
                    DocumentChange.Type.MODIFIED -> {
                        val index = systemList.indexOfFirst { it.id == systemItem.id }
                        Log.d(Constants.TAG, "system changed at ${index}")
                        systemList[index] = systemItem
                        notifyItemChanged(index)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SystemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.system_card_view, parent, false)
        return SystemViewHolder(view, this)
    }

    override fun getItemCount(): Int {
        return systemList.size
    }

    override fun onBindViewHolder(holder: SystemViewHolder, position: Int) {
        holder.bind(systemList[position])
    }

    public fun sendAddRequest(request: SystemRequest){
        requestRef.add(request)
    }

    fun onSystemPressed(pos: Int) {
        listener?.onSystemSelected(systemList.get(pos))
    }
}