package edu.rosehulman.kniermj.reignandroidapp.Login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.rosehulman.kniermj.reignandroidapp.Constants
import edu.rosehulman.kniermj.reignandroidapp.R
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.fragment_splash_screen.view.*

class SplashLoginFragment: Fragment() {
    var listener: LoginButtonListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(Constants.TAG, "creating login splash screen")
        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)
        view.login_button.setOnClickListener {
            listener?.onLoginButtonPressed()
        }
        view.rose_fire_login_button.setOnClickListener({
            listener?.onRoseFireButtonPressed()
        })
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as LoginButtonListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

}