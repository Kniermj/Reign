package edu.rosehulman.kniermj.reignandroidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import edu.rosehulman.kniermj.reignandroidapp.ComandQueue.CommandQueueListFragment
import edu.rosehulman.kniermj.reignandroidapp.HistoryGraph.GraphFragment
import edu.rosehulman.kniermj.reignandroidapp.HistoryQueue.CommandHistoryListFragment
import edu.rosehulman.kniermj.reignandroidapp.Login.LoginButtonListener
import edu.rosehulman.kniermj.reignandroidapp.Login.SplashLoginFragment
import edu.rosehulman.kniermj.reignandroidapp.Processes.ProcessListFragment
import edu.rosehulman.kniermj.reignandroidapp.SystemInfo.OnSystemScreenChange
import edu.rosehulman.kniermj.reignandroidapp.SystemInfo.SystemInfoFragment
import edu.rosehulman.kniermj.reignandroidapp.SystemList.ComputerSystem
import edu.rosehulman.kniermj.reignandroidapp.SystemList.OnSystemSelectlistener
import edu.rosehulman.kniermj.reignandroidapp.SystemList.SystemListFragment
import edu.rosehulman.rosefire.Rosefire

class MainActivity : AppCompatActivity(),
    LoginButtonListener,
    OnSystemSelectlistener,
    OnSystemScreenChange{

    private val auth = FirebaseAuth.getInstance()
    lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                true
            }
            R.id.action_logout ->{
                auth.signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }

    override fun onLoginButtonPressed() {
        buildGoogleLoginUI()
    }

    override fun onRoseFireButtonPressed() {
        rosefireLogin()
    }

    override fun onSystemSelected(sys: ComputerSystem) {
        switchToSystemInfo(sys)
    }

    override fun onCommandQueueSelected(sysId: String) {
        switchToCommandQueue(sysId)
    }
    override fun onCommandHistorySelected(sysId: String) {
        switchToCommandHistory(sysId)
    }

    override fun onProcessesSelected(sysId: String) {
        switchToProcessesFragment(sysId)
    }

    override fun onHistoryGraphPressed(sysId: String) {
        switchToGraphFragment(sysId)
    }

    private fun rosefireLogin() {
        val signInIntent = Rosefire.getSignInIntent(this, getString(R.string.rose_fire_token))
        startActivityForResult(signInIntent, Constants.RC_ROSEFIRE_LOGIN)
    }

    private fun buildGoogleLoginUI() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val loginIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        startActivityForResult(loginIntent, Constants.RC_SIGN_IN)
    }

    private fun initializeListeners() {
        authStateListener = FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
            val user = auth.currentUser
            Log.d(Constants.TAG, "in the auth listener and the user is $user")
            if (user != null) {
                SwitchToDefault(user.uid)
            } else {
                switchToLogin()
            }
        }
    }

    private fun switchToLogin() {
        Log.d(Constants.TAG, "user not logged in going to login screen")
        val mainFragment = SplashLoginFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_fragment, mainFragment)
        ft.commit()
    }

    private fun SwitchToDefault(uid: String) {
        Log.d(Constants.TAG, "user logged in going to default screen")
        Log.d(Constants.TAG, "new login with UID: $uid")
        val mainFragment = SystemListFragment.newInstance(uid)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_fragment, mainFragment)
        ft.commit()
    }

    private fun switchToSystemInfo(sys: ComputerSystem){
        val systemFragment = SystemInfoFragment.newInstance(sys)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_fragment, systemFragment)
        ft.addToBackStack("save")
        ft.commit()
    }

    private fun switchToCommandQueue(sysId: String) {
        val systemFragment = CommandQueueListFragment.newInstance(sysId)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_fragment, systemFragment)
        ft.addToBackStack("save")
        ft.commit()
    }

    private fun switchToCommandHistory(sysId: String) {
        Log.d(Constants.TAG, "switching to output fragment")
        val systemFragment = CommandHistoryListFragment.newInstance(sysId)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_fragment, systemFragment)
        ft.addToBackStack("save")
        ft.commit()
    }

    private fun switchToProcessesFragment(sysId: String) {
        Log.d(Constants.TAG, "switching to processes fragment")
        val processFragment = ProcessListFragment.newInstance(sysId)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_fragment, processFragment)
        ft.addToBackStack("save")
        ft.commit()
    }

    private fun switchToGraphFragment(sysId: String) {
        Log.d(Constants.TAG, "switching to history fragment")
        val graphFragment = GraphFragment.newInstance(sysId)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_fragment, graphFragment)
        ft.addToBackStack("save")
        ft.commit()
    }
}
