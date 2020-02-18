package edu.rosehulman.kniermj.reignandroidapp.HistoryQueue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.kniermj.reignandroidapp.ComandQueue.CommandQueueListAdapter
import edu.rosehulman.kniermj.reignandroidapp.ComandQueue.CommandQueueListFragment
import edu.rosehulman.kniermj.reignandroidapp.R
import edu.rosehulman.kniermj.reignandroidapp.Utlis.SwipeDeletionHelper
import kotlinx.android.synthetic.main.fragment_system_list_view.view.*

class CommandHistoryListFragment: Fragment() {
    var sysId: String? = null
    var adapter: CommandHistoryListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            sysId = it.getString(CommandHistoryListFragment.ARG_SYS)
        }
        val view = inflater.inflate(R.layout.fragment_command_history_list, container, false)

        adapter = CommandHistoryListAdapter(sysId ?: "", context!!, view)
        view.recycler_view.adapter = adapter
        view.recycler_view.layoutManager = LinearLayoutManager(activity)

        val swipeHelper = SwipeDeletionHelper(adapter!!)
        val helper = ItemTouchHelper(swipeHelper)
        helper.attachToRecyclerView(view.recycler_view)

        setHasOptionsMenu(true)

        return view
    }


    companion object {

        private const val ARG_SYS= "SYS_ID_ARG"

        @JvmStatic
        fun newInstance(systemID: String) =
            CommandHistoryListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SYS, systemID)
                }
            }
    }
}