package edu.rosehulman.kniermj.reignandroidapp.SystemInfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.rosehulman.kniermj.reignandroidapp.Constants
import edu.rosehulman.kniermj.reignandroidapp.R
import edu.rosehulman.kniermj.reignandroidapp.SystemList.ComputerSystem
import edu.rosehulman.kniermj.reignandroidapp.SystemList.SystemListFragment

class SystemInfoFragment: Fragment() {

    private var system: ComputerSystem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            system = it.getParcelable(ARG_SYS)
        }
        Log.d(Constants.TAG, "system info created with system: ${system?.id}")
        val view = inflater.inflate(R.layout.fragment_system_view, container, false)
        return view
    }

    companion object {

        private const val ARG_SYS= "SYS_ARG"

        @JvmStatic
        fun newInstance(system: ComputerSystem) =
            SystemInfoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_SYS, system)
                }
            }
    }
}