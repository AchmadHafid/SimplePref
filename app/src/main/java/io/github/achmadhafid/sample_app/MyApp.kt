package io.github.achmadhafid.sample_app

import android.app.Application
import io.github.achmadhafid.simplepref.converter.onDeserialize
import io.github.achmadhafid.simplepref.converter.onSerialize
import io.github.achmadhafid.simplepref.converter.simplePrefAddConverter
import io.github.achmadhafid.simplepref.lifecycle.SimplePrefApplication
import io.github.achmadhafid.simplepref.lifecycle.SimplePrefLifecycleOwner
import io.github.achmadhafid.simplepref.simplePref
import io.github.achmadhafid.zpack.ktx.applyTheme

class MyApp : Application(), SimplePrefLifecycleOwner by SimplePrefApplication() {

    private var appTheme: Int? by simplePref("app_theme")

    override fun onCreate() {
        super.onCreate()
        simplePrefAddConverter<MutableList<String>> {
            onSerialize {
                it.joinToString("::")
            }
            onDeserialize {
                if (it.isEmpty()) mutableListOf()
                else it.split("::").toMutableList()
            }
        }
        attachSimplePrefContext(this)
        appTheme?.let { applyTheme(it) }
    }
}
