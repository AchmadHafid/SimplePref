package io.github.achmadhafid.sample_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import io.github.achmadhafid.simplepref.SimplePref
import io.github.achmadhafid.simplepref.core.simplePrefClear
import io.github.achmadhafid.simplepref.core.simplePrefClearAllLocal
import io.github.achmadhafid.simplepref.core.simplePrefSave
import io.github.achmadhafid.simplepref.livedata.simplePrefLiveData
import io.github.achmadhafid.simplepref.simplePref
import io.github.achmadhafid.zpack.ktx.f
import io.github.achmadhafid.zpack.ktx.onSingleClick
import kotlin.random.Random

class ChildActivityFragment : Fragment(R.layout.fragment_child_activity), SimplePref {

    //region Preference

    private var myInt: Int? by simplePref()
    private val myList by globalPrefMyList()

    //endregion
    //region View Binding

    private var tvLocalVar: TextView? = null
    private var tvGlobalVar: TextView? = null
    private var btnAdd: MaterialButton? = null
    private var btnClearLocal: MaterialButton? = null
    private var btnClearGlobal: MaterialButton? = null

    //endregion

    //region Lifecycle Callback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tvLocalVar = view.f(R.id.tvLocalVar)
        tvGlobalVar = view.f(R.id.tvGlobalVar)
        btnAdd = view.f(R.id.btnAdd)
        btnClearLocal = view.f(R.id.btnClearLocal)
        btnClearGlobal = view.f(R.id.btnClearGlobal)

        simplePrefLiveData(myInt, ::myInt).observe(viewLifecycleOwner, Observer { updateUi() })
        simplePrefLiveData(myList, ::myList).observe(viewLifecycleOwner, Observer { updateUi() })

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
