package edu.rosehulman.kniermj.reignandroidapp.SystemList

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.common.io.Resources
import com.google.common.io.Resources.getResource
import edu.rosehulman.kniermj.reignandroidapp.Constants
import edu.rosehulman.kniermj.reignandroidapp.R
import kotlinx.android.synthetic.main.system_card_view.view.*

class SystemViewHolder: RecyclerView.ViewHolder {

    var nameText = itemView.system_name
    var activeText = itemView.system_active
    var context: Context? =  null


    constructor(itemView: View, adapter: SystemListAdapter, context: Context): super(itemView){
        this.context = context
        itemView.setOnClickListener { _: View? ->
            Log.d(Constants.TAG, "system clicked")
            adapter.onSystemPressed(adapterPosition)
            true
        }

    }

    fun bind(system: ComputerSystem) {
        nameText.setText(system.name)
        if(system.active){
            val color = ContextCompat.getColor(context!!, R.color.colorPrimary)
            itemView.main_system_card_layout.setBackgroundColor(color)
            activeText.setText(R.string.active)
        }else{
            val color = ContextCompat.getColor(context!!, R.color.colorPrimaryDark)
            itemView.main_system_card_layout.setBackgroundColor(color)
            activeText.setText(R.string.inactive)
        }
    }
}