package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

internal fun ByteArray.toHexString(): String {
    val builder = StringBuilder(size * 2)
    for(byte in this) {
        val i = byte and 0xFF
        builder.append(((i and 0xF0) shr 4).toHexString())
        builder.append((i and 0x0F).toHexString())
    }
    return builder.toString()
}

@Suppress("NOTHING_TO_INLINE")
internal inline fun Int.toHexString(): String = Integer.toHexString(this)

@Suppress("NOTHING_TO_INLINE")
internal inline infix fun Byte.and(b: Int) = JavaAnd.and(this, b)
