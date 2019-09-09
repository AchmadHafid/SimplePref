package io.github.achmadhafid.simplepref.livedata

import androidx.lifecycle.LiveData
import io.github.achmadhafid.simplepref.core.SimplePrefHolder

class SimplePrefNullableLiveData<V : Any>(private val holderKey: String) : LiveData<V?>() {

    private fun onDataChange(data: V?) {
        value = data
    }

    override fun onActive() {
        SimplePrefHolder.registerObserverNullable(holderKey, ::onDataChange)
    }

    override fun onInactive() {
        SimplePrefHolder.removeObserver(holderKey)
    }

}
