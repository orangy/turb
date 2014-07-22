package org.jetbrains.turb

public class Signal<TValue>(private val attached: (Scope, (TValue) -> Unit) -> Unit = {(l, f) -> }) {
    private val listeners = arrayListOf<(TValue) -> Unit>()

    public fun attach(scope: Scope, action: (TValue) -> Unit) {
        listeners.add(scope, action)
        attached(scope, action)
    }

    public fun invoke(value: TValue): Unit = fire(value)
    public fun fire(value: TValue) {
        val capture = listeners.toList()
        capture.forEach { it(value) }
    }
}