package org.jetbrains.turb

public fun <T> MutableList<T>.add(lifetime: Lifetime, value: T): Unit = lifetime.within({ add(value) }, { remove(value) })
