package org.jetbrains.turb

public class LifetimeDefinition : Disposable {
    public val lifetime: Lifetime = Lifetime(this)
    private val listeners = arrayListOf<() -> Unit>()
    private var isDisposed = false

    public fun plusAssign(end: () -> Unit): Unit = attach(end)

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

public class Lifetime(internal val definition: LifetimeDefinition) {
    public fun plusAssign(end: () -> Unit): Unit = attach(end)
    public fun attach(end: () -> Unit) {
        definition.attach(end)
    }
}

public fun Lifetime.attach(disposable: Disposable) {
    attach { disposable.dispose() }
}

public fun Lifetime.nested(): LifetimeDefinition {
    val def = LifetimeDefinition()
    val fn = { def.dispose() }
    def.attach {
        definition.detach(fn)
    }
    attach(fn)
    return def
}

public fun Lifetime.intersect(other: Lifetime): LifetimeDefinition {
    val def = LifetimeDefinition()
    val fn = { def.dispose() }
    def.attach {
        other.definition.detach(fn)
        definition.detach(fn)
    }
    attach(fn)
    other.attach(fn)
    return def
}
