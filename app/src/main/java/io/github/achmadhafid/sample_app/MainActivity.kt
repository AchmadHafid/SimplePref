package io.github.achmadhafid.sample_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import io.github.achmadhafid.sample_app.databinding.ActivityMainBinding
import io.github.achmadhafid.simplepref.SimplePref
import io.github.achmadhafid.simplepref.core.simplePrefClear
import io.github.achmadhafid.simplepref.core.simplePrefSave
import io.github.achmadhafid.simplepref.livedata.simplePrefLiveData
import io.github.achmadhafid.simplepref.simplePref
import io.github.achmadhafid.zpack.extension.intent
import io.github.achmadhafid.zpack.extension.startService
import io.github.achmadhafid.zpack.extension.stopService
import io.github.achmadhafid.zpack.extension.toggleTheme
import kotlin.random.Random

class MainActivity : AppCompatActivity(), SimplePref {

    //region Preference

    private var appTheme: Int? by simplePref("app_theme")
    private var myInt: Int? by simplePref()
    private val myList by globalPrefMyList()
    private var isServiceRunning by globalPrefIsServiceRunning()

    //endregion
    //region View Binding

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //endregion
    //region Lifecycle Callback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.btnOpenChildActivity.setOnClickListener {
            startActivity(intent<ChildActivity>())
        }
        binding.btnOpenFragmentActivity.setOnClickListener {
            startActivity(intent<ChildActivityWithFragment>())
        }

        simplePrefLiveData(isServiceRunning, ::isServiceRunning)
            .observe(this, { updateUi() })
        simplePrefLiveData(myInt, ::myInt)
            .observe(this, { updateUi() })
        simplePrefLiveData(myList, ::myList)
            .observe(this, { updateUi() })

        binding.content.btnAdd.setOnClickListener {
            @Suppress("MagicNumber")
            myInt = Random.nextInt(1, 100)
            myList.add("$myInt")
            simplePrefSave(::myList)
        }
        binding.content.btnClearLocal.setOnClickListener {
            simplePrefClear(::myInt)
        }
        binding.content.btnClearGlobal.setOnClickListener {
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
        binding.btnToggleService.text =
            if (isServiceRunning) "Stop Observer Service"
            else "Start Observer Service"
        binding.btnToggleService.setOnClickListener {
            if (isServiceRunning) stopService<LiveDataObserverService>()
            else startService<LiveDataObserverService>()
        }
        binding.content.tvLocalVar.text  = "myInt (Local pref) : $myInt"
        binding.content.tvGlobalVar.text = "myList Size (Global pref): ${myList.size}"
    }

    //endregion

}
