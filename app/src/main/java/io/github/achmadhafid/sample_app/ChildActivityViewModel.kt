package io.github.achmadhafid.sample_app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.achmadhafid.simplepref.SimplePref
import io.github.achmadhafid.simplepref.core.simplePrefClear
import io.github.achmadhafid.simplepref.core.simplePrefClearAllLocal
import io.github.achmadhafid.simplepref.core.simplePrefSave
import io.github.achmadhafid.simplepref.lifecycle.SimplePrefLifecycleOwner
import io.github.achmadhafid.simplepref.lifecycle.SimplePrefViewModel
import io.github.achmadhafid.simplepref.livedata.simplePrefLiveData
import io.github.achmadhafid.simplepref.simplePref
import kotlin.random.Random

class ChildActivityViewModel(application: Application) : AndroidViewModel(application), SimplePref,
    SimplePrefLifecycleOwner by SimplePrefViewModel(application) {

    private var myInt: Int? by simplePref()
    private val myList by globalPrefMyList()

    fun getMyInt() =
        simplePrefLiveData(myInt, ::myInt)

    fun getMyList() =
        simplePrefLiveData(myList, ::myList)

    fun changeValue() {
        myInt = Random.nextInt(1, 100)
        myList.add("$myInt")
        simplePrefSave(::myList)
    }

    fun clearAllLocal() {
        simplePrefClearAllLocal()
    }

    fun clearMyList() {
        simplePrefClear(::myList)
    }

    override fun onCleared() {
        onDestroySimplePref()
        super.onCleared()
    }
}

//region Factory

class ChildActivityViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ChildActivityViewModel(application) as T
    }
}

//endregion
