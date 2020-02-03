package edu.rosehulman.kniermj.reignandroidapp.SystemList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.kniermj.reignandroidapp.R

class SystemListAdapter(
    var context: Context,
    var uid: String):
    RecyclerView.Adapter<SystemViewHolder>() {

    var systemList = ArrayList<System>()

    init{
        systemList.add(System("Temp System", "High", "Active"))
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
}