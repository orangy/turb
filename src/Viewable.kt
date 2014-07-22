package org.jetbrains.turb

public trait Viewable<T> {
    public fun view(scope: Scope, action: (Scope, T) -> Unit)
}

fun <T> Viewable<T>.filter(predicate: (T) -> Boolean): Viewable<T> = object : Viewable<T> {
    override fun view(scope: Scope, action: (Scope, T) -> Unit) {
        this@filter.view(scope) {(s, v) ->
            if (predicate(v)) {
                action(s, v)
            }
        }
    }
}

fun <T, V> Viewable<T>.map(transform: (T) -> V): Viewable<V> = object : Viewable<V> {
    override fun view(scope: Scope, action: (Scope, V) -> Unit) {
        this@map.view(scope) {(s, v) ->
            action(s, transform(v))
        }
    }
}