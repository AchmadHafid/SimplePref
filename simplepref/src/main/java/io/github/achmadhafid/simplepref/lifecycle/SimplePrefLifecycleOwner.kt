package io.github.achmadhafid.simplepref.lifecycle

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import io.github.achmadhafid.simplepref.SimplePref

interface SimplePrefLifecycleOwner: SimplePref, LifecycleOwner {

    var context: Context?
    fun attachSimplePrefContext(context: Context)
    fun onCreateSimplePref()
    fun onDestroySimplePref()

}
