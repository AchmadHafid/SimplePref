package io.github.achmadhafid.simplepref.lifecycle

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry

class SimplePrefLifecycleOwnerImpl(override var context: Application? = null): SimplePrefLifecycleOwner {

    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    init {
        context?.let {
            onCreateSimplePref()
        }
    }

    override fun attachSimplePrefContext(application: Application) {
        if (context == null) {
            context = application
            onCreateSimplePref()
        }
    }

    override fun getLifecycle() = lifecycleRegistry

    override fun onCreateSimplePref() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onDestroySimplePref() {
        context = null
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

}
