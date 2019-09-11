package io.github.achmadhafid.sample_app

import android.app.Application
import android.text.TextUtils
import io.github.achmadhafid.simplepref.converter.onDeserialize
import io.github.achmadhafid.simplepref.converter.onSerialize
import io.github.achmadhafid.simplepref.converter.simplePrefAddConverter

class MyApp : Application() {

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
