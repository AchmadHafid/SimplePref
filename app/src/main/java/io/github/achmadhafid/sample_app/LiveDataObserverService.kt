package io.github.achmadhafid.sample_app

import android.app.Service
import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import io.github.achmadhafid.simplepref.SimplePref
import io.github.achmadhafid.simplepref.livedata.simplePrefLiveData
import io.github.achmadhafid.zpack.extension.toastShort

class LiveDataObserverService : LifecycleService(), SimplePref {

    //region Preference

    private var isRunning by globalPrefIsServiceRunning()
    private val myList by globalPrefMyList()

    //endregion

    //region Lifecycle Callback

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (!isRunning) {
            isRunning = true

            simplePrefLiveData(myList, ::myList).observe(this, Observer {
                toastShort("<From Service> myList size: ${it.size}")
            })
        }

        return Service.START_STICKY
    }

    override fun onDestroy() {
        isRunning = false
        super.onDestroy()
    }

    //endregion
}
