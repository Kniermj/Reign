package edu.rosehulman.kniermj.reignandroidapp.SystemList

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.system_card_view.view.*

class SystemViewHolder : RecyclerView.ViewHolder {

    var nameText = itemView.system_name
    var usageText = itemView.system_usage
    var activeText = itemView.system_active


    constructor(itemView: View, adapter: SystemListAdapter): super(itemView){

    }

    fun bind(system: System) {
        nameText.setText(system.name)
        usageText.setText(system.usage)
        activeText.setText(system.active)
    }
}