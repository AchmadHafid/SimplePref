package io.github.achmadhafid.sample_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import io.github.achmadhafid.simplepref.SimplePref
import io.github.achmadhafid.simplepref.core.simplePrefClear
import io.github.achmadhafid.simplepref.core.simplePrefClearAllGlobal
import io.github.achmadhafid.simplepref.core.simplePrefClearAllLocal
import io.github.achmadhafid.simplepref.core.simplePrefSave
import io.github.achmadhafid.simplepref.livedata.simplePrefLiveData
import io.github.achmadhafid.simplepref.simplePref
import io.github.achmadhafid.zpack.delegate.fragmentView
import io.github.achmadhafid.zpack.ktx.onSingleClick
import kotlin.random.Random

class ChildActivityFragment : Fragment(R.layout.fragment_child_activity), SimplePref {

    //region Preference

    private var myInt: Int? by simplePref()
    private val myList by globalPrefMyList()

    //endregion
    //region View Binding

    private var tvLocalVar: TextView? by fragmentView(R.id.tvLocalVar)
    private var tvGlobalVar: TextView? by fragmentView(R.id.tvGlobalVar)
    private var btnAdd: MaterialButton? by fragmentView(R.id.btnAdd)
    private var btnClearLocal: MaterialButton? by fragmentView(R.id.btnClearLocal)
    private var btnClearGlobal: MaterialButton? by fragmentView(R.id.btnClearGlobal)

    //endregion

    //region Lifecycle Callback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        simplePrefLiveData(myInt, ::myInt) { updateUi() }
        simplePrefLiveData(myList, ::myList) { updateUi() }

        btnAdd?.onSingleClick(true) {
            @Suppress("MagicNumber")
            myInt = Random.nextInt(1, 100)
            myList.add("$myInt")
            simplePrefSave(::myList)
        }
        btnClearLocal?.onSingleClick(true) {
            simplePrefClearAllLocal()
        }
        btnClearGlobal?.onSingleClick(true) {
            simplePrefClear(::myList)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi() {
        tvLocalVar?.text  = "myInt (Local pref) : $myInt"
        tvGlobalVar?.text = "myList Size (Global pref): ${myList.size}"
    }

    //endregion

}
