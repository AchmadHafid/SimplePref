package io.github.achmadhafid.simplepref.converter

import kotlin.reflect.KClass

data class SimplePrefConverter<V : Any>(
    internal var clazz: KClass<V>,
    internal var signature: Int = clazz.java.name.hashCode(),
    internal var serializer: ((V) -> String)? = null,
    internal var deserializer: ((String) -> V)? = null
)

//region Extensions

inline fun <reified V : Any> simplePrefAddConverter(
    builder: SimplePrefConverter<V>.() -> Unit
) {
    SimplePrefConverterHolder.addConverter(
        SimplePrefConverter(V::class)
            .apply(builder)
    )
}

fun <V : Any> SimplePrefConverter<V>.onSerialize(block: (V) -> String) {
    serializer = block
}

fun <V : Any> SimplePrefConverter<V>.onDeserialize(block: (String) -> V) {
    deserializer = block
}

//endregion
