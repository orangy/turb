package org.jetbrains.turb

public fun <T> MutableList<T>.add(scope: Scope, value: T): Unit = scope.within({ add(value) }, { remove(value) })
