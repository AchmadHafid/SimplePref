@file:Suppress("TooManyFunctions")

package io.github.achmadhafid.simplepref.core

import io.github.achmadhafid.simplepref.SimplePref
import kotlin.reflect.KProperty

internal object SimplePrefHolder {

    private val nonNullGlobalPrefs  = hashMapOf<String, BaseSharedPreferencesNonNull<*, *>?>()
    private val nonNullLocalPrefs   = hashMapOf<String, BaseSharedPreferencesNonNull<*, *>?>()
    private val nullableGlobalPrefs = hashMapOf<String, BaseSharedPreferencesNullable<*, *>?>()
    private val nullableLocalPrefs  = hashMapOf<String, BaseSharedPreferencesNullable<*, *>?>()

    fun <T : Any> getKey(thisRef: T, property: KProperty<*>): String =
        thisRef.javaClass.name + property.name

    //region Add - Remove Preference Holder

    fun addNonNullPref(key: String, item: BaseSharedPreferencesNonNull<*, *>, isGlobal: Boolean) {
        if (isGlobal)
            nonNullGlobalPrefs[key] = item
        else
            nonNullLocalPrefs[key] = item
    }

    fun addNullablePref(key: String, item: BaseSharedPreferencesNullable<*, *>, isGlobal: Boolean) {
        if (isGlobal)
            nullableGlobalPrefs[key] = item
        else
            nullableLocalPrefs[key] = item
    }

    fun removePref(vararg keys: String) {
        keys.forEach {
            nonNullGlobalPrefs.remove(it)
            nonNullLocalPrefs.remove(it)
            nullableGlobalPrefs.remove(it)
            nullableLocalPrefs.remove(it)
        }
    }

    //endregion
    //region Save - Clear Preferences

    fun <T : Any> save(thisRef: T, vararg properties: KProperty<*>) {
        properties.forEach {
            val key = getKey(thisRef, it)

            nonNullGlobalPrefs[key]?.save() ?: nonNullLocalPrefs[key]?.save()
            ?: nullableGlobalPrefs[key]?.save() ?: nullableLocalPrefs[key]?.save()
        }
    }

    private fun clear(keys: List<String>) {
        keys.forEach {
            nonNullGlobalPrefs[it]?.clear()
            nonNullLocalPrefs[it]?.clear()
            nullableGlobalPrefs[it]?.clear()
            nullableLocalPrefs[it]?.clear()
        }
    }

    fun <T : Any> clear(thisRef: T, vararg properties: KProperty<*>) {
        clear(properties.map { getKey(thisRef, it) })
    }

    fun <T : Any> clearAllLocal(thisRef: T) {
        nonNullLocalPrefs.keys.forEach {
            if (it.startsWith(thisRef.javaClass.name)) {
                nonNullLocalPrefs[it]?.clear()
            }
        }
        nullableLocalPrefs.keys.forEach {
            if (it.startsWith(thisRef.javaClass.name)) {
                nullableLocalPrefs[it]?.clear()
            }
        }
    }

    fun clearAllGlobal() {
        nonNullGlobalPrefs.values.forEach { it?.clear() }
        nullableGlobalPrefs.values.forEach { it?.clear() }
    }

    fun clearAll() {
        clearAllGlobal()
        nullableGlobalPrefs.values.forEach { it?.clear() }
        nullableLocalPrefs.values.forEach { it?.clear() }
    }

    //endregion
    //region Observer

    @Suppress("UNCHECKED_CAST")
    fun <V : Any> registerObserverNonNull(key: String, observer: (V) -> Unit) {
        (nonNullGlobalPrefs[key] as? BaseSharedPreferencesNonNull<*, V>)?.registerObserver(
            key,
            observer
        )
        (nonNullLocalPrefs[key] as? BaseSharedPreferencesNonNull<*, V>)?.registerObserver(
            key,
            observer
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun <V : Any> registerObserverNullable(key: String, observer: (V?) -> Unit) {
        (nullableGlobalPrefs[key] as? BaseSharedPreferencesNullable<*, V>)?.registerObserver(
            key,
            observer
        )
        (nullableLocalPrefs[key] as? BaseSharedPreferencesNullable<*, V>)?.registerObserver(
            key,
            observer
        )
    }

    fun removeObserver(key: String) {
        nonNullGlobalPrefs[key]?.removeObserver(key)
        nonNullLocalPrefs[key]?.removeObserver(key)
        nullableGlobalPrefs[key]?.removeObserver(key)
        nullableLocalPrefs[key]?.removeObserver(key)
    }

    //endregion

}

//region Extensions

fun SimplePref.simplePrefSave(property: KProperty<*>) {
    SimplePrefHolder.save(this, property)
}

@Suppress("SpreadOperator")
fun SimplePref.simplePrefClear(vararg properties: KProperty<*>) {
    SimplePrefHolder.clear(this, *properties)
}

fun SimplePref.simplePrefClearAllLocal() {
    SimplePrefHolder.clearAllLocal(this)
}

fun SimplePref.simplePrefClearAllGlobal() {
    SimplePrefHolder.clearAllGlobal()
}

fun SimplePref.simplePrefClearAll() {
    SimplePrefHolder.clearAll()
}

//endregion
