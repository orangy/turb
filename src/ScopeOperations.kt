package org.jetbrains.turb

public inline fun Scope.within(enter: () -> Unit, noinline leave: () -> Unit) {
    enter()
    attach(leave)
}

public inline fun scope<R>(body: Scope.() -> R): R {
    val def = ScopeDefinition()
    try {
        return def.scope.body()
    } finally {
        def.dispose()
    }
}

public fun Scope.nested(): ScopeDefinition {
    val def = ScopeDefinition()
    val fn = { def.dispose() }
    def.attach {
        detach(fn)
    }
    attach(fn)
    return def
}

public fun Scope.intersect(other: Scope): ScopeDefinition {
    val def = ScopeDefinition()
    val fn = { def.dispose() }
    def.attach {
        other.detach(fn)
        detach(fn)
    }
    attach(fn)
    other.attach(fn)
    return def
}
