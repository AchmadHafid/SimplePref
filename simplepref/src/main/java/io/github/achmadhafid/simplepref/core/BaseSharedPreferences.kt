@file:Suppress("TooManyFunctions")

package io.github.achmadhafid.simplepref.core

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.github.achmadhafid.simplepref.converter.SimplePrefConverterHolder
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

private const val SHARED_PREFERENCES_NAME = "simple_pref"

abstract class BaseSharedPreferences<T : Any, V: Any>(
    private val getSharedPreferences: (T, String, Boolean) -> SharedPreferences?,
    private val getLifecycle: (T) -> Lifecycle?,
    private val clazz: KClass<V>,
    protected val globalKey: String? = null
) {

    private var sharedPreferences: SharedPreferences? = null
    private val sharedPreferencesObserver: SharedPreferences.OnSharedPreferenceChangeListener by lazy {
        SharedPreferences.OnSharedPreferenceChangeListener { _, changeKey ->
            if (changeKey == key) onDataChange()
        }
    }
    private var lifecycle: Lifecycle? = null
    private lateinit var key: String

    abstract fun onAttach(key: String)
    abstract fun onDetach(key: String)
    abstract fun onDataChange()

    private fun init(thisRef: T, property: KProperty<*>) {
        key = globalKey ?: property.name
        if (sharedPreferences == null) {
            val prefName = SHARED_PREFERENCES_NAME +
                    if (globalKey != null) ""
                    else thisRef.javaClass.simpleName
            sharedPreferences = getSharedPreferences(
                thisRef, prefName, globalKey != null
            )
        }
        if (lifecycle == null) {
            val holderKey =
                SimplePrefHolder.getKey(thisRef, property)
            lifecycle = getLifecycle(thisRef)?.apply {
                addObserver(object : LifecycleObserver {
                    @Suppress("unused")
                    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
                    fun onCreate() {
                        sharedPreferences?.registerOnSharedPreferenceChangeListener(
                            sharedPreferencesObserver
                        )
                        onAttach(holderKey)
                    }

                    @Suppress("unused")
                    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                    fun onDestroy() {
                        sharedPreferences?.unregisterOnSharedPreferenceChangeListener(
                            sharedPreferencesObserver
                        )
                        lifecycle?.removeObserver(this)
                        sharedPreferences = null
                        lifecycle = null
                        onDetach(holderKey)
                    }
                })
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <V : Any> retrieveNonNull(
        thisRef: T,
        property: KProperty<*>,
        defaultValue: V
    ): V = retrieveNullable(thisRef, property, defaultValue) as V

    @Suppress("UNCHECKED_CAST")
    protected fun <V : Any> retrieveNonNull(defaultValue: V): V =
        retrieveNullable(defaultValue) as V

    @Suppress("ComplexMethod", "UNCHECKED_CAST")
    protected fun <V : Any> retrieveNullable(
        thisRef: T,
        property: KProperty<*>,
        defaultValue: V? = null
    ): V? {
        init(thisRef, property)
        return retrieveNullable(defaultValue)
    }

    @Suppress("ComplexMethod", "UNCHECKED_CAST")
    protected fun <V : Any> retrieveNullable(defaultValue: V? = null): V? =
        if (!::key.isInitialized) {
            defaultValue
        } else {
            when (clazz) {
                Boolean::class -> {
                    if (isExist(key)) {
                        read { getBoolean(key, false) } as V?
                    } else {
                        defaultValue
                    }
                }
                Int::class -> {
                    if (isExist(key)) {
                        read { getInt(key, 0) } as V?
                    } else {
                        defaultValue
                    }
                }
                Long::class -> {
                    if (isExist(key)) {
                        read { getLong(key, 0L) } as V?
                    } else {
                        defaultValue
                    }
                }
                Float::class -> {
                    if (isExist(key)) {
                        read { getFloat(key, 0F) } as V?
                    } else {
                        defaultValue
                    }
                }
                String::class -> {
                    if (isExist(key)) {
                        read { getString(key, "") } as V?
                    } else {
                        defaultValue
                    }
                }
                else -> if (isExist(key)) {
                    read {
                        SimplePrefConverterHolder.deserialize(
                            key,
                            getString(key, "") ?: "",
                            clazz
                        )
                    } as V?
                } else {
                    defaultValue
                }
            }
        }

    protected fun <V : Any> save(thisRef: T, property: KProperty<*>, value: V?) {
        init(thisRef, property)
        save(value)
    }

    @Suppress("ComplexMethod", "UNCHECKED_CAST")
    protected fun <V : Any> save(value: V?) {
        value?.let {
            when (it) {
                is Boolean -> write { putBoolean(key, it as Boolean) }
                is Int -> write { putInt(key, it as Int) }
                is Long -> write { putLong(key, it as Long) }
                is Float -> write { putFloat(key, it as Float) }
                is String -> write { putString(key, it as String) }
                else -> write {
                    putString(key, SimplePrefConverterHolder.serialize(value, clazz))
                }
            }
        } ?: write { remove(key) }
    }

    //region SharedPreferences Helper

    private fun isExist(key: String) =
        sharedPreferences!!.contains(key)

    private fun <V> read(block: SharedPreferences.() -> V?): V? =
        sharedPreferences?.let(block)

    private fun write(block: SharedPreferences.Editor.() -> Unit) =
        sharedPreferences?.edit(action = block)

    //endregion

}
