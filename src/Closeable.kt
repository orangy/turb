package org.jetbrains.turb

import java.io.Closeable

public fun <T : Closeable> Scope.auto(closeable: T): T {
    attach { closeable.close() }
    return closeable
}

public fun <T : Closeable> T.auto(scope: Scope): T {
    scope.attach { close() }
    return this
}

class MockClosable(val name: String) : Closeable {
    {
        println("$name opened.")
    }

    override fun close() {
        println("$name closed.")
    }

    override fun toString(): String {
        return "$name"
    }
}

fun useClosable(value: Closeable) {
}

class MyStorage(val param: Closeable) : Closeable {
    val scope = ScopeDefinition()
    val stream = MockClosable("class").auto(scope.scope)

    override fun close() {
        scope.dispose()
        println("storage closed.")
    }

    override fun toString(): String {
        return "storage"
    }
}

fun main(args: Array<String>) {
    val value: Int = scope {
        val stream = auto(MockClosable("file"))
        val mock = MockClosable("reader1").auto(this)
        useClosable(auto(MyStorage(mock)))
        println("still using $stream")
        1
    }
    println(value)
}
