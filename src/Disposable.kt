package org.jetbrains.turb

public trait Disposable {
    public fun dispose()
}

public inline fun using(disposable: Disposable, body: () -> Unit) {
    try {
        body()
    } finally {
        disposable.dispose()
    }
}

public class ObjectDisposedException(message : String = "Object already disposed.") : Exception(message)