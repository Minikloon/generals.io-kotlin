package net.minikloon.generals.utils

import kotlin.reflect.KProperty

fun <T> versionLazy(versionProvider: () -> Int, initializer: () -> T): VersionLazyDelegate<T> = VersionLazyDelegate(versionProvider, initializer)

class VersionLazyDelegate<out T>(private val versionProvider: () -> Int, private val valueProvider: () -> T) {
    private var version = -1
    private var value: T? = null
    
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val expectedVersion = versionProvider()
        if(expectedVersion != version) {
            value = null
            version = expectedVersion
        }
        
        if(value == null)
            value = valueProvider()
        return value!!
    }
}