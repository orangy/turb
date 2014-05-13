package org.jetbrains.turb

public trait Viewable<T> {
    public fun view(lifetime: Lifetime, observer: (Lifetime, T) -> Unit)
}