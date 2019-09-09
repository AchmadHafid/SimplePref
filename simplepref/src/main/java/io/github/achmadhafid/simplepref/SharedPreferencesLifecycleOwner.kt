package io.github.achmadhafid.simplepref

import android.content.Context
import android.content.SharedPreferences
import io.github.achmadhafid.simplepref.core.BaseSharedPreferencesNonNull
import io.github.achmadhafid.simplepref.core.BaseSharedPreferencesNullable
import io.github.achmadhafid.simplepref.lifecycle.SimplePrefLifecycleOwner
import kotlin.reflect.KClass

class LifecycleOwnerSharedPreferencesNonNull<LO : SimplePrefLifecycleOwner, V : Any>(
    globalKey: String? = null,
    clazz: KClass<V>,
    defaultValue: () -> V
) : BaseSharedPreferencesNonNull<LO, V>(
    ::getSharedPreferences,
    ::getLifecycle,
    globalKey, clazz, defaultValue
)

class LifecycleOwnerSharedPreferencesNullable<LO : SimplePrefLifecycleOwner, V : Any>(
    globalKey: String? = null,
    clazz: KClass<V>
) : BaseSharedPreferencesNullable<LO, V>(
    ::getSharedPreferences,
    ::getLifecycle,
    globalKey, clazz
)

private fun <LO : SimplePrefLifecycleOwner> getSharedPreferences(
    viewModel: LO,
    prefName: String,
    @Suppress("UNUSED_PARAMETER") isGlobalPref: Boolean
): SharedPreferences? = viewModel.context?.getSharedPreferences(prefName, Context.MODE_PRIVATE)

private fun <LO : SimplePrefLifecycleOwner> getLifecycle(viewModel: LO) = viewModel.lifecycle

//region Extensions

inline fun <LO : SimplePrefLifecycleOwner, reified V : Any> LO.simplePref(
    globalKey: String? = null,
    noinline defaultValue: () -> V
) =
    LifecycleOwnerSharedPreferencesNonNull<LO, V>(globalKey, V::class, defaultValue)

inline fun <LO : SimplePrefLifecycleOwner, reified V : Any> LO.simplePref(
    globalKey: String? = null
) =
    LifecycleOwnerSharedPreferencesNullable<LO, V>(globalKey, V::class)

//endregion
