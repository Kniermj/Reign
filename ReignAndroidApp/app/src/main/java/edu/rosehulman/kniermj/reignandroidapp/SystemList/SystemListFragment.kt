package edu.rosehulman.kniermj.reignandroidapp.SystemList

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
import kotlinx.android.synthetic.main.fragment_system_list_view.view.*

class SystemListFragment: Fragment() {

    var uid: String? = null
    var adapter: SystemListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            uid = it.getString(ARG_UID)
        }
        val view = inflater.inflate(R.layout.fragment_system_list_view, container, false)

        adapter = SystemListAdapter(context!!,uid ?: "")
        view.recycler_view.adapter = adapter
        view.recycler_view.layoutManager = LinearLayoutManager(activity)
        setHasOptionsMenu(true)

        view.fab.setOnClickListener({
            Log.d(Constants.TAG, "clicked")
        })

        return view
    }

    companion object {

        private const val ARG_UID= "UID_ARG"

        @JvmStatic
        fun newInstance(uid: String) =
            SystemListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_UID, uid)
                }
            }
    }
}