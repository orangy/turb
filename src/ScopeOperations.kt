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

public inline fun Scope.scope<R>(body: Scope.() -> R): R = nested().scope.body()

public fun Scope.nested(): ScopeDefinition {
    val def = ScopeDefinition()
    val defDispose = { def.dispose() }
    def.attach {
        detach(defDispose)
    }
    attach(defDispose)
    return def
}

public fun Scope.intersect(other: Scope): ScopeDefinition {
    val def = ScopeDefinition()
    val defDispose = { def.dispose() }
    def.attach {
        other.detach(defDispose)
        detach(defDispose)
    }
    attach(defDispose)
    other.attach(defDispose)
    return def
}
