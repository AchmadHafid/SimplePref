package io.github.achmadhafid.sample_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import io.github.achmadhafid.simplepref.SimplePref
import io.github.achmadhafid.simplepref.core.simplePrefClear
import io.github.achmadhafid.simplepref.core.simplePrefClearAllLocal
import io.github.achmadhafid.simplepref.core.simplePrefSave
import io.github.achmadhafid.simplepref.livedata.simplePrefLiveData
import io.github.achmadhafid.simplepref.simplePref
import io.github.achmadhafid.zpack.ktx.bindView
import io.github.achmadhafid.zpack.ktx.onSingleClick
import io.github.achmadhafid.zpack.ktx.setMaterialToolbar
import kotlin.random.Random

class ChildActivity : AppCompatActivity(R.layout.activity_child), SimplePref {

    //region Preference

    private var myInt: Int? by simplePref()
    private val myList by globalPrefMyList()

    //endregion
    //region View Binding

    private val tvLocalVar: TextView by bindView(R.id.tvLocalVar)
    private val tvGlobalVar: TextView by bindView(R.id.tvGlobalVar)
    private val btnAdd: MaterialButton by bindView(R.id.btnAdd)
    private val btnClearLocal: MaterialButton by bindView(R.id.btnClearLocal)
    private val btnClearGlobal: MaterialButton by bindView(R.id.btnClearGlobal)

    //endregion

    //region Lifecycle Callback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMaterialToolbar(R.id.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        simplePrefLiveData(myInt, ::myInt) { updateUi() }
        simplePrefLiveData(myList, ::myList) { updateUi() }

        btnAdd.onSingleClick(true) {
            @Suppress("MagicNumber")
            myInt = Random.nextInt(1, 100)
            myList.add("$myInt")
            simplePrefSave(::myList)
        }
        btnClearLocal.onSingleClick(true) {
            simplePrefClearAllLocal()
        }
        btnClearGlobal.onSingleClick(true) {
            simplePrefClear(::myList)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi() {
        tvLocalVar.text  = "myInt (Local pref) : $myInt"
        tvGlobalVar.text = "myList Size (Global pref): ${myList.size}"
    }

    //endregion

}
