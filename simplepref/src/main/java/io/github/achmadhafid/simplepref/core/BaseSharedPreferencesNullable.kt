package io.github.achmadhafid.simplepref.core

import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

abstract class BaseSharedPreferencesNullable<T : Any, V : Any>(
    getSharedPreferences: (T, String, Boolean) -> SharedPreferences?,
    getLifecycle: (T) -> Lifecycle?,
    globalKey: String? = null,
    clazz: KClass<V>
) : BaseSharedPreferences<T, V>(getSharedPreferences, getLifecycle, clazz, globalKey),
    ReadWriteProperty<T, V?> {

    private var backingField: V? = null

    override fun onAttach(key: String) {
        SimplePrefHolder.addNullablePref(
            key,
            this,
            globalKey != null
        )
    }

    override fun onDetach(key: String) {
        backingField = null
        SimplePrefHolder.removePref(key)
    }

    fun getValue(): V? {
        backingField = retrieveNullable<V>(null)
        return backingField
    }

    override fun getValue(thisRef: T, property: KProperty<*>): V? {
        backingField = retrieveNullable<V>(thisRef, property, null)
        return backingField
    }

    override fun setValue(thisRef: T, property: KProperty<*>, value: V?) {
        backingField = value
        save(thisRef, property, backingField)
    }

    fun save() {
        save(backingField)
    }

    fun clear() {
        backingField = null
        save(backingField)
    }

    //region Observer Helper

    private val observers = mutableMapOf<String, (V?) -> Unit>()

    override fun onDataChange() {
        observers.values.forEach {
            it(getValue())
        }
    }

    fun registerObserver(key: String, observer: (V?) -> Unit) {
        if (!observers.containsKey(key)) {
            observers[key] = observer
            observer(getValue())
        }
    }

    fun removeObserver(key: String) {
        observers.remove(key)
    }

    //endregion

}
