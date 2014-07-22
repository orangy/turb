package org.jetbrains.turb

public class ScopeDefinition : Disposable {
    public val scope: Scope = Scope(this)
    private val listeners = arrayListOf<() -> Unit>()
    private var isDisposed = false

    public fun plusAssign(end: () -> Unit): Unit = attach(end)
    public fun minusAssign(end: () -> Unit): Unit = detach(end)

    public fun attach(end: () -> Unit) {
        if (disposed)
            throw ObjectDisposedException()
        listeners.add(end)
    }

    public fun detach(end: () -> Unit) {
        if (disposed)
            throw ObjectDisposedException()
        listeners.remove(end)
    }

    public val disposed: Boolean
        get() = isDisposed

    public override fun dispose() {
        if (disposed)
            throw ObjectDisposedException()

        val capture = listeners.reverse()
        capture.forEach { it() }
        isDisposed = true
    }
}

public class Scope(private val definition: ScopeDefinition) {
    public fun plusAssign(end: () -> Unit): Unit = attach(end)
    public fun minusAssign(end: () -> Unit): Unit = detach(end)

    public fun attach(end: () -> Unit) {
        definition.attach(end)
    }
    public fun detach(end: () -> Unit) {
        definition.detach(end)
    }
}