package io.github.achmadhafid.simplepref.livedata

import io.github.achmadhafid.simplepref.SimplePref
import io.github.achmadhafid.simplepref.core.SimplePrefHolder
import kotlin.reflect.KProperty

fun <V : Any> SimplePref.simplePrefLiveData(
    @Suppress("UNUSED_PARAMETER") variable: V,
    property: KProperty<*>
) = SimplePrefNonNullLiveData<V>(
    SimplePrefHolder.getKey(this, property)
)

fun <V : Any> SimplePref.simplePrefLiveData(
    @Suppress("UNUSED_PARAMETER") variable: V?,
    property: KProperty<*>
) = SimplePrefNullableLiveData<V>(
    SimplePrefHolder.getKey(this, property)
)
