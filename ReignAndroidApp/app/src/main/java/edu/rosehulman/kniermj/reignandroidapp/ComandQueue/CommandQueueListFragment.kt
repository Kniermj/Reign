package edu.rosehulman.kniermj.reignandroidapp.ComandQueue

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.kniermj.reignandroidapp.Constants
import edu.rosehulman.kniermj.reignandroidapp.R
import edu.rosehulman.kniermj.reignandroidapp.Utlis.SwipeDeletionHelper
import kotlinx.android.synthetic.main.dialog_add_command.view.*
import kotlinx.android.synthetic.main.fragment_command_queue_list.view.*


class CommandQueueListFragment: Fragment() {

    var sysId: String? = null
    var adapter: CommandQueueListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            sysId = it.getString(CommandQueueListFragment.ARG_SYS)
        }
        val view = inflater.inflate(R.layout.fragment_command_queue_list, container, false)

        adapter = CommandQueueListAdapter(sysId ?: "", context!!, view)
        view.recycler_view.adapter = adapter
        view.recycler_view.layoutManager = LinearLayoutManager(activity)

        view.fab.setOnClickListener({
            Log.d(Constants.TAG, "clicked")
            displayAddCommandDialog()
        })

        val swipeHelperDel = SwipeDeletionHelper(adapter!!)
        val helperDel = ItemTouchHelper(swipeHelperDel)
        helperDel.attachToRecyclerView(view.recycler_view)


        return view
    }

    private fun displayAddCommandDialog(){
        val builder = androidx.appcompat.app.AlertDialog.Builder(context!!)
        builder.setTitle("Add a command")
        val view = LayoutInflater.from(context).inflate(
            R.layout.dialog_add_command, null, false
        )
        builder.setView(view)
        builder.setIcon(android.R.drawable.ic_input_add)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val commandString = view.command_entry.text.toString()
            val newCommand = CommandQueueItem(commandString, "", adapter?.itemCount ?: 0)
            adapter?.addCommandToQueue(newCommand);

        }

        builder.setNegativeButton(android.R.string.cancel, null)
        builder.show()
    }

    companion object {

        private const val ARG_SYS= "SYS_ID_ARG"

        @JvmStatic
        fun newInstance(systemID: String) =
            CommandQueueListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SYS, systemID)
                }
            }
    }
}
