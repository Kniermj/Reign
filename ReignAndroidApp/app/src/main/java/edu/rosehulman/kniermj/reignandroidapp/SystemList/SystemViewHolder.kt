package edu.rosehulman.kniermj.reignandroidapp.SystemList

import android.graphics.Color
import android.provider.SyncStateContract
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.kniermj.reignandroidapp.Constants
import kotlinx.android.synthetic.main.system_card_view.view.*

class SystemViewHolder : RecyclerView.ViewHolder {

    var nameText = itemView.system_name
    var usageText = itemView.system_usage
    var activeText = itemView.system_active


    constructor(itemView: View, adapter: SystemListAdapter): super(itemView){
        itemView.setOnClickListener { v: View? ->
            Log.d(Constants.TAG, "system clicked")
            adapter.onSystemPressed(adapterPosition)
            true
        }

    }

    fun bind(system: ComputerSystem) {
        nameText.setText(system.name)
        usageText.setText("high")
        activeText.setText(system.active.toString())

        if(system.active){
            itemView.setBackgroundColor(Color.WHITE)
        }else{
            itemView.setBackgroundColor(Color.RED)
        }
    }
}