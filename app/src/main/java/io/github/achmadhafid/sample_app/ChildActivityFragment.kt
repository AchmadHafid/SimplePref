package io.github.achmadhafid.sample_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.achmadhafid.sample_app.databinding.FragmentChildActivityBinding
import io.github.achmadhafid.simplepref.SimplePref
import io.github.achmadhafid.simplepref.core.simplePrefClear
import io.github.achmadhafid.simplepref.core.simplePrefClearAllLocal
import io.github.achmadhafid.simplepref.core.simplePrefSave
import io.github.achmadhafid.simplepref.livedata.simplePrefLiveData
import io.github.achmadhafid.simplepref.simplePref
import io.github.achmadhafid.zpack.delegate.lifecycleVar
import kotlin.random.Random

class ChildActivityFragment : Fragment(), SimplePref {

    //region Preference

    private var myInt: Int? by simplePref()
    private val myList by globalPrefMyList()

    //endregion
    //region View Binding

    private var _binding by lifecycleVar<FragmentChildActivityBinding>()
    private val binding get() = _binding!!

    //endregion
    //region Lifecycle Callback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChildActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        simplePrefLiveData(myInt, ::myInt).observe(viewLifecycleOwner) { updateUi() }
        simplePrefLiveData(myList, ::myList).observe(viewLifecycleOwner) { updateUi() }

        binding.content.btnAdd.setOnClickListener {
            @Suppress("MagicNumber")
            myInt = Random.nextInt(1, 100)
            myList.add("$myInt")
            simplePrefSave(::myList)
        }
        binding.content.btnClearLocal.setOnClickListener {
            simplePrefClearAllLocal()
        }
        binding.content.btnClearGlobal.setOnClickListener {
            simplePrefClear(::myList)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi() {
        binding.content.tvLocalVar.text = "myInt (Local pref) : $myInt"
        binding.content.tvGlobalVar.text = "myList Size (Global pref): ${myList.size}"
    }

    //endregion

}
