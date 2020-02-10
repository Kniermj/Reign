package edu.rosehulman.kniermj.reignandroidapp.HistoryQueue

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.kniermj.reignandroidapp.ComandQueue.CommandQueueItem
import edu.rosehulman.kniermj.reignandroidapp.ComandQueue.CommandQueueListAdapter
import kotlinx.android.synthetic.main.command_history_card_view.view.*
import kotlinx.android.synthetic.main.command_queue_card_view.view.*

class CommandHistoryHolder: RecyclerView.ViewHolder {

    var commandInputEntry = itemView.command_history_input
    var commandOutputEntry = itemView.command_history_output

    constructor(itemView: View, adapter: CommandHistoryListAdapter): super(itemView){

    }

    public fun bind(item: CommandQueueItem){
        commandInputEntry.setText(item.commandInput)
        commandOutputEntry.setText(item.commandOutput)
    }
}