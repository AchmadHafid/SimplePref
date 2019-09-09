package io.github.achmadhafid.simplepref.converter

import kotlin.reflect.KClass

object SimplePrefConverterHolder {

    private val converters = mutableMapOf<KClass<*>, SimplePrefConverter<*>>()

    fun addConverter(converter: SimplePrefConverter<*>) {
        converters[converter.clazz] = converter
    }

    @Suppress("UNCHECKED_CAST")
    fun <V : Any> serialize(value: V, clazz: KClass<*>): String =
        (converters[clazz] as? SimplePrefConverter<V>)?.let {
            it.serializer?.invoke(value) + formatSignatureMark(it.signature)
        } ?: TODO("<SimplePref> No serializer found for $clazz")

    @Suppress("UNCHECKED_CAST")
    fun <V : Any> deserialize(key: String, value: String, clazz: KClass<V>): V =
        (converters[clazz] as? SimplePrefConverter<V>)?.let {
            checkSignature(key, value, it.signature)
            it.deserializer?.invoke(value.replace(formatSignatureMark(it.signature), ""))
        } ?: TODO("<SimplePref> No deserializer found for $clazz")

    private fun checkSignature(key: String, value: String, signature: Int) {
        with(value.substringAfterLast(SIGNATURE_MARK, "")) {
            require(isNotEmpty()) {
                "<SimplePref> This shared preference is corrupt"
            }
            require (this == "$signature") {
                "<SimplePref> Other global preference with key `$key` but different type already exist"
            }
        }
    }

    private fun formatSignatureMark(signature: Int) = "$SIGNATURE_MARK${signature}"

    private const val SIGNATURE_MARK = "[{*SP*}]"

}
