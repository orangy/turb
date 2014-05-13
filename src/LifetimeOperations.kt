package org.jetbrains.turb

public inline fun Lifetime.within(enter: ()->Unit, noinline leave : ()->Unit) {
    enter()
    attach(leave)
}

public fun Lifetime.attach(disposable: Disposable) {
    attach { disposable.dispose() }
}

public fun Lifetime.nested(): LifetimeDefinition {
    val def = LifetimeDefinition()
    val fn = { def.dispose() }
    def.attach {
        detach(fn)
    }
    attach(fn)
    return def
}

public fun Lifetime.intersect(other: Lifetime): LifetimeDefinition {
    val def = LifetimeDefinition()
    val fn = { def.dispose() }
    def.attach {
        other.detach(fn)
        detach(fn)
    }
    attach(fn)
    other.attach(fn)
    return def
}
