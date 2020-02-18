package edu.rosehulman.kniermj.reignandroidapp.ComandQueue

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.kniermj.reignandroidapp.Constants
import edu.rosehulman.kniermj.reignandroidapp.SystemList.SystemListAdapter
import kotlinx.android.synthetic.main.command_queue_card_view.view.*

class CommandQueueHolder: RecyclerView.ViewHolder {

    var commandEntry = itemView.command_input_text

    constructor(itemView: View, adapter: CommandQueueListAdapter): super(itemView){

        itemView.moveUp.setOnClickListener {
            adapter.moveItemUp(adapterPosition)
        }
        itemView.moveDown.setOnClickListener {
            adapter.moveItemDown(adapterPosition)
        }

        itemView.setOnLongClickListener {
            adapter.editCommand(adapterPosition)
            true
        }

    }

    public fun bind(item: CommandQueueItem){
        commandEntry.setText(item.commandInput)
    }
}