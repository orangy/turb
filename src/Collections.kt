package org.jetbrains.turb

fun <T> MutableList<T>.add(lifetime: Lifetime, value: T) {
    add(value)
    lifetime += { remove(value) }
}