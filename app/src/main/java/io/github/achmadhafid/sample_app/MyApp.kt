package io.github.achmadhafid.sample_app

import android.app.Application
import android.text.TextUtils
import io.github.achmadhafid.simplepref.converter.onDeserialize
import io.github.achmadhafid.simplepref.converter.onSerialize
import io.github.achmadhafid.simplepref.converter.simplePrefAddConverter
import io.github.achmadhafid.simplepref.lifecycle.SimplePrefLifecycleOwner
import io.github.achmadhafid.simplepref.lifecycle.SimplePrefLifecycleOwnerImpl

class MyApp : Application(), SimplePrefLifecycleOwner by SimplePrefLifecycleOwnerImpl() {

    override fun onCreate() {
        super.onCreate()
        simplePrefAddConverter<MutableList<String>> {
            onSerialize {
                TextUtils.join("::", it)
            }
            onDeserialize {
                if (it.isEmpty()) mutableListOf()
                else it.split("::").toMutableList()
            }
        }
    }
}
