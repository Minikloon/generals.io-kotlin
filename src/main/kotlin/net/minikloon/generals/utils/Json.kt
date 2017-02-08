package net.minikloon.generals.utils

import org.json.JSONArray

fun JSONArray.toIntArray() : IntArray {
    val array = IntArray(length())
    (0 until length()).forEach { i ->
        array[i] = getInt(i)
    }
    return array
}

inline fun <reified T: Any> JSONArray.toTypedArray() : Array<T> {
    return (0 until length()).mapNotNull { i ->
        get(i) as T
    }.toTypedArray()
}