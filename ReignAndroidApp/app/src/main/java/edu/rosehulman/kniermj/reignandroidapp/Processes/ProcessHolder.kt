package edu.rosehulman.kniermj.reignandroidapp.Processes

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.process_card_view.view.*

class ProcessHolder: RecyclerView.ViewHolder {

    var processNameEntry = itemView.process_name_text
    var processIdEntry = itemView.process_id_text

    constructor(itemView: View, adapter: ProcessListAdapter): super(itemView){
        itemView.setOnLongClickListener {
            adapter.startProcessKillDialog(adapterPosition)
            true
        }
    }

    public fun bind(item: ProcessItem){
        processIdEntry.setText(item.processId)
        processNameEntry.setText(item.processName)
    }
}