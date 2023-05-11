package io.github.achmadhafid.simplepref.lifecycle

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry

class SimplePrefLifecycleOwnerImpl(override var context: Context? = null) :
    SimplePrefLifecycleOwner {

    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override fun attachSimplePrefContext(context: Context) {
        if (this.context == null) {
            this.context = context
            onCreateSimplePref()
        }
    }

    override fun onCreateSimplePref() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onDestroySimplePref() {
        context = null
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    init {
        context?.let { onCreateSimplePref() }
    }

}

typealias SimplePrefViewModel   = SimplePrefLifecycleOwnerImpl
typealias SimplePrefApplication = SimplePrefLifecycleOwnerImpl
