package io.github.achmadhafid.simplepref

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import io.github.achmadhafid.simplepref.core.BaseSharedPreferencesNonNull
import io.github.achmadhafid.simplepref.core.BaseSharedPreferencesNullable
import kotlin.reflect.KClass

class ServiceSharedPreferencesNonNull<S : LifecycleService, V : Any>(
    globalKey: String? = null,
    clazz: KClass<V>,
    defaultValue: () -> V
) : BaseSharedPreferencesNonNull<S, V>(
    ::getSharedPreferences,
    ::getLifecycle,
    globalKey, clazz, defaultValue
)

class ServiceSharedPreferencesNullable<S : LifecycleService, V : Any>(
    globalKey: String? = null,
    clazz: KClass<V>
) : BaseSharedPreferencesNullable<S, V>(
    ::getSharedPreferences,
    ::getLifecycle,
    globalKey, clazz
)

private fun <S : LifecycleService> getSharedPreferences(
    thisRef: S,
    prefName: String,
    @Suppress("UNUSED_PARAMETER") isGlobalPref: Boolean
): SharedPreferences? = thisRef.getSharedPreferences(prefName, Context.MODE_PRIVATE)

private fun <S : LifecycleService> getLifecycle(thisRef: S): Lifecycle = thisRef.lifecycle

//region Extensions

inline fun <S, reified V : Any> S.simplePref(
    globalKey: String? = null,
    noinline defaultValue: () -> V
) where S : SimplePref, S : LifecycleService =
    ServiceSharedPreferencesNonNull<S, V>(globalKey, V::class, defaultValue)

inline fun <S, reified V : Any> S.simplePref(
    globalKey: String? = null
) where S : SimplePref, S : LifecycleService =
    ServiceSharedPreferencesNullable<S, V>(globalKey, V::class)

//endregion
