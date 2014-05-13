package org.jetbrains.turb

public class LifetimeDefinition : Disposable {
    public val lifetime: Lifetime = Lifetime(this)
    private val attached = arrayListOf<() -> Unit>()
    private var disposed = false

    public fun plusAssign(end: () -> Unit): Unit = attach(end)

    public fun attach(end: () -> Unit) {
        if (disposed)
            throw ObjectDisposedException()
        attached.add(end)
    }

    public fun detach(end: () -> Unit) {
        if (disposed)
            throw ObjectDisposedException()
        attached.remove(end)
    }

    public override fun dispose() {
        if (disposed)
            throw ObjectDisposedException()

        val capture = attached.toList()
        capture.forEach { it() }
        disposed = true
    }
}

public class Lifetime(internal val definition: LifetimeDefinition) {
    public fun attach(end: () -> Unit) {
        definition.attach(end)
    }
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
