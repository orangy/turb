package org.jetbrains.turb

public trait Viewable<T> {
    public fun view(lifetime: Lifetime, observer: (Lifetime, T) -> Unit)
}

fun <T> Viewable<T>.filter(predicate: (T) -> Boolean): Viewable<T> = object : Viewable<T> {
    override fun view(lifetime: Lifetime, observer: (Lifetime, T) -> Unit) {
        this@filter.view(lifetime) {(l, v) ->
            if (predicate(v)) {
                observer(l, v)
            }
        }
    }
}

fun <T, V> Viewable<T>.map(transform: (T) -> V): Viewable<V> = object : Viewable<V> {
    override fun view(lifetime: Lifetime, observer: (Lifetime, V) -> Unit) {
        this@map.view(lifetime) {(l, v) ->
            observer(l, transform(v))
        }
    }
}