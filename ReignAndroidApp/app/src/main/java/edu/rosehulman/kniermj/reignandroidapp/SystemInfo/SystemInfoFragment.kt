package edu.rosehulman.kniermj.reignandroidapp.SystemInfo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.kniermj.reignandroidapp.Constants
import edu.rosehulman.kniermj.reignandroidapp.R
import edu.rosehulman.kniermj.reignandroidapp.SystemList.ComputerSystem
import kotlinx.android.synthetic.main.fragment_system_view.view.*

class SystemInfoFragment: Fragment() {

    private var system: ComputerSystem? = null
    private var listener: OnSystemScreenChange? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            system = it.getParcelable(ARG_SYS)
        }
        Log.d(Constants.TAG, "system info created with system: ${system.toString()}")
        val view = inflater.inflate(R.layout.fragment_system_view, container, false)
        view.system_view_name.setText(system?.name)
        view.button_queue_view.setOnClickListener {
            listener?.onCommandQueueSelected(system?.id ?: "")
        }
        view.button_output_view.setOnClickListener{
            Log.d(Constants.TAG, "history clicked}")
            listener?.onCommandHistorySelected(system?.id ?: "")
        }

        queueSizeListener(view)
        historySizeListener(view)

        return view
    }

    private fun queueSizeListener(view: View){
        val queueRef = FirebaseFirestore.getInstance()
            .collection("systems")
            .document(system!!.id)
            .collection("CommandQueue")
        queueRef.addSnapshotListener { snapshot, exception ->
            if (exception != null){
                Log.d(Constants.TAG, exception.toString())
                return@addSnapshotListener
            }
            view.queue_size_text.setText(snapshot?.size().toString())
        }
    }
    private fun historySizeListener(view: View){
        val histRef = FirebaseFirestore.getInstance()
            .collection("systems")
            .document(system!!.id)
            .collection("CommandHistory")
        histRef.addSnapshotListener { snapshot, exception ->
            if (exception != null){
                Log.d(Constants.TAG, exception.toString())
                return@addSnapshotListener
            }
            view.history_size_text.setText(snapshot?.size().toString())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSystemScreenChange) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {

        private const val ARG_SYS= "SYS_OBJ_ARG"

        @JvmStatic
        fun newInstance(system: ComputerSystem) =
            SystemInfoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_SYS, system)
                }
            }
    }
}