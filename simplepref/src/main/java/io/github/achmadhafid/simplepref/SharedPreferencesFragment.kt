package io.github.achmadhafid.simplepref

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import io.github.achmadhafid.simplepref.core.BaseSharedPreferencesNonNull
import io.github.achmadhafid.simplepref.core.BaseSharedPreferencesNullable
import kotlin.reflect.KClass

class FragmentSharedPreferencesNonNull<F : Fragment, V : Any>(
    globalKey: String? = null,
    clazz: KClass<V>,
    defaultValue: () -> V
) : BaseSharedPreferencesNonNull<F, V>(
    ::getSharedPreferences,
    ::getLifecycle,
    globalKey, clazz, defaultValue
)

class FragmentSharedPreferencesNullable<F : Fragment, V : Any>(
    globalKey: String? = null,
    clazz: KClass<V>
) : BaseSharedPreferencesNullable<F, V>(
    ::getSharedPreferences,
    ::getLifecycle,
    globalKey, clazz
)

private fun <F : Fragment> getSharedPreferences(
    thisRef: F,
    prefName: String,
    @Suppress("UNUSED_PARAMETER") isGlobalPref: Boolean
): SharedPreferences? = thisRef.context?.getSharedPreferences(prefName, Context.MODE_PRIVATE)

private fun <F : Fragment> getLifecycle(thisRef: F): Lifecycle = thisRef.lifecycle

//region Extensions

inline fun <F, reified V : Any> F.simplePref(
    globalKey: String? = null,
    noinline defaultValue: () -> V
) where F : SimplePref, F : Fragment =
    FragmentSharedPreferencesNonNull<Fragment, V>(globalKey, V::class, defaultValue)

inline fun <F, reified V : Any> F.simplePref(
    globalKey: String? = null
) where F : SimplePref, F : Fragment =
    FragmentSharedPreferencesNullable<F, V>(globalKey, V::class)

//endregion
