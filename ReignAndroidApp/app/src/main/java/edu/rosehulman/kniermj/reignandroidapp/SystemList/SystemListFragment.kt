package edu.rosehulman.kniermj.reignandroidapp.SystemList

import android.app.AlertDialog
import android.content.Context
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
import kotlinx.android.synthetic.main.dialog_add_system.view.*
import kotlinx.android.synthetic.main.fragment_system_list_view.view.*

class SystemListFragment: Fragment() {

    var uid: String? = null
    var adapter: SystemListAdapter? = null
    var listener: OnSystemSelectlistener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            uid = it.getString(ARG_UID)
        }
        Log.d(Constants.TAG, "uid from onCreate is: $uid")
        val view = inflater.inflate(R.layout.fragment_system_list_view, container, false)

        adapter = SystemListAdapter(context!!,uid ?: "", listener)
        view.recycler_view.adapter = adapter
        view.recycler_view.layoutManager = LinearLayoutManager(activity)
        setHasOptionsMenu(true)

        view.fab.setOnClickListener({
            Log.d(Constants.TAG, "clicked")
            startAddSystemDialog()
        })

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSystemSelectlistener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    private fun startAddSystemDialog(){
        val builder = androidx.appcompat.app.AlertDialog.Builder(context!!)
        builder.setTitle("Add a system")
        val view = LayoutInflater.from(context).inflate(
            R.layout.dialog_add_system, null, false
        )
        builder.setView(view)
        builder.setIcon(android.R.drawable.ic_input_add)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val systemID = view.system_id_entry.text.toString()
            val systemPassword = view.system_password_entry.text.toString()
            Log.d(Constants.TAG, "uid is: $uid")
            val request = SystemRequest(systemID, uid!!, systemPassword)
            adapter?.sendAddRequest(request)

        }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.show()
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