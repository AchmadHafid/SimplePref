package io.github.achmadhafid.simplepref

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import io.github.achmadhafid.simplepref.core.BaseSharedPreferencesNonNull
import io.github.achmadhafid.simplepref.core.BaseSharedPreferencesNullable
import kotlin.reflect.KClass

class ActivitySharedPreferencesNonNull<A : FragmentActivity, V : Any>(
    globalKey: String? = null,
    clazz: KClass<V>,
    defaultValue: () -> V
) : BaseSharedPreferencesNonNull<A, V>(
    ::getSharedPreferences,
    ::getLifecycle,
    globalKey, clazz, defaultValue
)

class ActivitySharedPreferencesNullable<A : FragmentActivity, V: Any>(
    globalKey: String? = null,
    clazz: KClass<V>
) : BaseSharedPreferencesNullable<A, V>(
    ::getSharedPreferences,
    ::getLifecycle,
    globalKey, clazz
)

private fun <A : FragmentActivity> getSharedPreferences(activity: A,
    prefName: String,
    isGlobalPref: Boolean
): SharedPreferences? = if (isGlobalPref)
    activity.getSharedPreferences(prefName, Context.MODE_PRIVATE)
else
    activity.getPreferences(Context.MODE_PRIVATE)

private fun <A : FragmentActivity> getLifecycle(activity: A) = activity.lifecycle

//region Extensions

inline fun <A, reified V : Any> A.simplePref(
    globalKey: String? = null,
    noinline defaultValue: () -> V
) where A : SimplePref, A : FragmentActivity =
    ActivitySharedPreferencesNonNull<A, V>(globalKey, V::class, defaultValue)

inline fun <A, reified V : Any> A.simplePref(
    globalKey: String? = null
) where A : SimplePref, A : FragmentActivity =
    ActivitySharedPreferencesNullable<A, V>(globalKey, V::class)

//endregion
