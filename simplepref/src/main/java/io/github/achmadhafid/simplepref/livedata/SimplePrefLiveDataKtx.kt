@file:Suppress("TooManyFunctions")

package io.github.achmadhafid.simplepref.livedata

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import io.github.achmadhafid.simplepref.SimplePref
import io.github.achmadhafid.simplepref.core.SimplePrefHolder
import kotlin.reflect.KProperty

fun <V : Any> SimplePref.simplePrefLiveData(
    @Suppress("UNUSED_PARAMETER") variable: V,
    property: KProperty<*>
) = SimplePrefNonNullLiveData<V>(
    SimplePrefHolder.getKey(
        this,
        property
    )
)

fun <V : Any> SimplePref.simplePrefLiveData(
    @Suppress("UNUSED_PARAMETER") variable: V?,
    property: KProperty<*>
) = SimplePrefNullableLiveData<V>(
    SimplePrefHolder.getKey(
        this,
        property
    )
)

fun <A, V : Any> A.simplePrefLiveData(
    @Suppress("UNUSED_PARAMETER") variable: V,
    property: KProperty<*>,
    observer: (V) -> Unit
) where A : SimplePref, A : FragmentActivity =
    simplePrefLiveData(variable, property).apply {
        observe(this@simplePrefLiveData, Observer { observer(it) })
    }

fun <A, V : Any> A.simplePrefLiveData(
    @Suppress("UNUSED_PARAMETER") variable: V?,
    property: KProperty<*>,
    observer: (V?) -> Unit
) where A : SimplePref, A : FragmentActivity =
    simplePrefLiveData(variable, property).apply {
        observe(this@simplePrefLiveData, Observer { observer(it) })
    }

fun <F, V : Any> F.simplePrefLiveData(
    @Suppress("UNUSED_PARAMETER") variable: V,
    property: KProperty<*>,
    observer: (V) -> Unit
) where F : SimplePref, F : Fragment =
    simplePrefLiveData(variable, property).apply {
        observe(this@simplePrefLiveData.viewLifecycleOwner, Observer { observer(it) })
    }

fun <F, V : Any> F.simplePrefLiveData(
    @Suppress("UNUSED_PARAMETER") variable: V?,
    property: KProperty<*>,
    observer: (V?) -> Unit
) where F : SimplePref, F : Fragment =
    simplePrefLiveData(variable, property).apply {
        observe(this@simplePrefLiveData.viewLifecycleOwner, Observer { observer(it) })
    }

fun <S, V : Any> S.simplePrefLiveData(
    @Suppress("UNUSED_PARAMETER") variable: V,
    property: KProperty<*>,
    observer: (V) -> Unit
) where S : SimplePref, S : LifecycleService =
    simplePrefLiveData(variable, property).apply {
        observe(this@simplePrefLiveData, Observer { observer(it) })
    }

fun <S, V : Any> S.simplePrefLiveData(
    @Suppress("UNUSED_PARAMETER") variable: V?,
    property: KProperty<*>,
    observer: (V?) -> Unit
) where S : SimplePref, S : LifecycleService =
    simplePrefLiveData(variable, property).apply {
        observe(this@simplePrefLiveData, Observer { observer(it) })
    }
