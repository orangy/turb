package org.jetbrains.turb

public trait Viewable<T> {
    public fun view(scope: Scope, action: (Scope, T) -> Unit)
}

fun <T> Viewable<T>.filter(predicate: (T) -> Boolean): Viewable<T> = object : Viewable<T> {
    override fun view(scope: Scope, action: (Scope, T) -> Unit) {
        this@filter.view(scope) {(l, v) ->
            if (predicate(v)) {
                action(l, v)
            }
        }
    }
}

fun <T, V> Viewable<T>.map(transform: (T) -> V): Viewable<V> = object : Viewable<V> {
    override fun view(scope: Scope, action: (Scope, V) -> Unit) {
        this@map.view(scope) {(l, v) ->
            action(l, transform(v))
        }
    }
}