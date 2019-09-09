package io.github.achmadhafid.simplepref.lifecycle

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import io.github.achmadhafid.simplepref.SimplePref

interface SimplePrefLifecycleOwner: SimplePref, LifecycleOwner {

    var context: Application?
    fun attachSimplePrefContext(application: Application)
    fun onCreateSimplePref()
    fun onDestroySimplePref()

}
