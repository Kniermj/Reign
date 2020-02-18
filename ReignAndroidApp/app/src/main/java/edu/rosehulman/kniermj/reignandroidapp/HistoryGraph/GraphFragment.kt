package edu.rosehulman.kniermj.reignandroidapp.HistoryGraph

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import edu.rosehulman.kniermj.reignandroidapp.Constants
import edu.rosehulman.kniermj.reignandroidapp.R
import edu.rosehulman.kniermj.reignandroidapp.SystemInfo.SystemStat
import kotlinx.android.synthetic.main.history_graph.view.*

class GraphFragment: Fragment() {

    var sysId: String? = null
    var cpuEntries = ArrayList<Entry>()
    var memEntries = ArrayList<Entry>()
    var statusReports = ArrayList<SystemStat>();
    var cpuFocus = true;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            sysId = it.getString(GraphFragment.ARG_SYS)
        }
        val view = inflater.inflate(R.layout.history_graph, container, false)
        view.history_line_chart.setTouchEnabled(true)
        view.history_line_chart.setPinchZoom(true)
        addListeners()
        setHasOptionsMenu(true)
        return view
    }

    private fun addListeners() {
        val statusRef = FirebaseFirestore.getInstance()
            .collection("systems")
            .document(sysId ?: "")
            .collection("Status")
        statusRef.orderBy("creation", Query.Direction.ASCENDING).addSnapshotListener { snapshot, exception ->

            if(exception != null){
                Log.d(Constants.TAG, exception.toString())
                return@addSnapshotListener
            }
            for (documentChange in snapshot?.documentChanges!!) {
                Log.d(Constants.TAG, "loading for graph")
                val item = SystemStat.fromSnapshot(documentChange.document)
                val xValue = item.creation?.seconds?.toFloat()
                val memPercent = (item.cpuUsage / (item.memUsage + item.memFree)) * 100
                when (documentChange.type) {
                    DocumentChange.Type.ADDED -> {
                        statusReports.add(item)
                        cpuEntries.add(Entry(xValue ?: 0f, item.cpuUsage))
                        memEntries.add(Entry(xValue ?: 0f, memPercent))
                    }
                    DocumentChange.Type.REMOVED -> {
                        val index = statusReports.indexOfFirst { it.id == item.id }
                        statusReports.removeAt(index)
                        cpuEntries.removeAt(index)
                        memEntries.removeAt(index)
                    }
                    DocumentChange.Type.MODIFIED -> {
                        val index = statusReports.indexOfFirst { it.id == item.id }
                        statusReports[index] = item
                        cpuEntries[index] = Entry(xValue ?: 0f, item.cpuUsage)
                        memEntries[index] = Entry(xValue ?: 0f, memPercent)
                    }
                }
                displayGraph()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(edu.rosehulman.kniermj.reignandroidapp.R.menu.menu_graph, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_graph -> {
                Log.d(Constants.TAG, "switch pressed")
                cpuFocus = !cpuFocus
                switchTile(item)
                displayGraph()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayGraph(){
        var set: LineDataSet? = null
        if(cpuFocus){
            set = LineDataSet(cpuEntries, "CPU")
        }else{
            set = LineDataSet(memEntries, "MEM")
        }
        set.setDrawFilled(true)
        view?.history_line_chart?.data = LineData(set)
        view?.history_line_chart?.notifyDataSetChanged()
        view?.history_line_chart?.invalidate()
    }

    private fun switchTile(item: MenuItem) {
        if(cpuFocus){
            item.title = "MEM"
        }else{
            item.title = "CPU"
        }
    }

    companion object {

        private const val ARG_SYS= "SYS_ID_ARG"

        @JvmStatic
        fun newInstance(systemID: String) =
            GraphFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SYS, systemID)
                }
            }
    }
}