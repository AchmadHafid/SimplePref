package io.github.achmadhafid.sample_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import io.github.achmadhafid.simplepref.SimplePref
import io.github.achmadhafid.simplepref.core.simplePrefClear
import io.github.achmadhafid.simplepref.core.simplePrefSave
import io.github.achmadhafid.simplepref.livedata.simplePrefLiveData
import io.github.achmadhafid.simplepref.simplePref
import io.github.achmadhafid.zpack.ktx.*
import kotlin.random.Random

class MainActivity : AppCompatActivity(R.layout.activity_main), SimplePref {

    //region Preference

    private var appTheme: Int? by simplePref("app_theme")
    private var myInt: Int? by simplePref()
    private val myList by globalPrefMyList()
    private var isServiceRunning by globalPrefIsServiceRunning()

    //endregion
    //region View Binding

    private val tvLocalVar: TextView by bindView(R.id.tvLocalVar)
    private val tvGlobalVar: TextView by bindView(R.id.tvGlobalVar)
    private val btnAdd: MaterialButton by bindView(R.id.btnAdd)
    private val btnClearLocal: MaterialButton by bindView(R.id.btnClearLocal)
    private val btnClearGlobal: MaterialButton by bindView(R.id.btnClearGlobal)
    private val btnToggleService: MaterialButton by bindView(R.id.btnToggleService)
    private val btnOpenChild: MaterialButton by bindView(R.id.btnOpenChildActivity)
    private val btnOpenFragmentActivity: MaterialButton by bindView(R.id.btnOpenFragmentActivity)

    //endregion

    //region Lifecycle Callback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMaterialToolbar(R.id.toolbar)

        btnOpenChild.onSingleClick(true) { startActivity<ChildActivity>() }
        btnOpenFragmentActivity.onSingleClick(true) { startActivity<ChildActivityWithFragment>() }

        simplePrefLiveData(isServiceRunning, ::isServiceRunning)
            .observe(this, Observer { updateUi() })
        simplePrefLiveData(myInt, ::myInt)
            .observe(this, Observer { updateUi() })
        simplePrefLiveData(myList, ::myList)
            .observe(this, Observer { updateUi() })

        btnAdd.onSingleClick(true) {
            @Suppress("MagicNumber")
            myInt = Random.nextInt(1, 100)
            myList.add("$myInt")
            simplePrefSave(::myList)
        }
        btnClearLocal.onSingleClick(true) {
            simplePrefClear(::myInt)
        }
        btnClearGlobal.onSingleClick(true) {
            simplePrefClear(::myList)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        if (isServiceRunning) stopService<LiveDataObserverService>()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle_theme -> {
                appTheme = toggleTheme()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi() {
        btnToggleService.text =
            if (isServiceRunning) "Stop Observer Service"
            else "Start Observer Service"
        btnToggleService.onSingleClick {
            if (isServiceRunning) stopService<LiveDataObserverService>()
            else startService<LiveDataObserverService>()
        }
        tvLocalVar.text  = "myInt (Local pref) : $myInt"
        tvGlobalVar.text = "myList Size (Global pref): ${myList.size}"
    }

    //endregion

}
