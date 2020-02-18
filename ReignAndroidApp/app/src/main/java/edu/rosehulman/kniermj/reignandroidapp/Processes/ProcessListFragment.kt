package edu.rosehulman.kniermj.reignandroidapp.Processes

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.kniermj.reignandroidapp.Constants
import edu.rosehulman.kniermj.reignandroidapp.R
import kotlinx.android.synthetic.main.fragment_system_list_view.view.*


class ProcessListFragment : Fragment() {

    var sysId: String? = null
    var adapter: ProcessListAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            sysId = it.getString(ProcessListFragment.ARG_SYS)
        }
        val view = inflater.inflate(R.layout.fragment_process_list, container, false)

        adapter = ProcessListAdapter(sysId ?: "", context!!)
        view.recycler_view.adapter = adapter
        view.recycler_view.layoutManager = LinearLayoutManager(activity)
        adapter?.refreshProcesses()
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(edu.rosehulman.kniermj.reignandroidapp.R.menu.menu_process, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_refresh_proc -> {
                Log.d(Constants.TAG, "refresh pressed")
                adapter?.refreshProcesses()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {

        private const val ARG_SYS= "SYS_ID_ARG"

        @JvmStatic
        fun newInstance(systemID: String) =
            ProcessListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SYS, systemID)
                }
            }
    }
}