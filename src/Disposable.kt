package org.jetbrains.turb

public trait Disposable {
    public fun dispose()
}

public fun Scope.auto<T : Disposable>(disposable: T): T {
    attach { disposable.dispose() }
    return disposable
}

public class ObjectDisposedException(message: String = "Object already disposed.") : Exception(message)