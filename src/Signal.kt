package org.jetbrains.turb

public class Signal<TValue> {
    private val listeners = arrayListOf<(TValue) -> Unit>()

    public fun attach(lifetime : Lifetime, action : (TValue)->Unit) {
        listeners.add(lifetime, action)
    }

    public fun invoke(value : TValue) : Unit = fire(value)
    public fun fire(value : TValue) {
        val capture = listeners.toList()
        capture.forEach { it(value) }
    }
}