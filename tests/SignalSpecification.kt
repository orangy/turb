package org.jetbrains.turb.tests

import org.jetbrains.turb.*
import org.spek.*

class SignalSpecification : Spek() {{

    given("a signal with attached handler") {
        var value = 0
        val signal = Signal<Int>()

        val def = ScopeDefinition()
        signal.attach(def.scope) {
            value++
        }
        on("invoking signal") {
            signal(1)
            it("should invoke handler once") {
                shouldEqual(1, value)
            }
        }
    }

    given("a signal with expired handler") {
        var value = 0
        val signal = Signal<Int>()

        val def = ScopeDefinition()
        signal.attach(def.scope) {
            value++
        }
        def.dispose()

        on("invoking signal") {
            signal(1)
            it("should not invoke handler") {
                shouldEqual(0, value)
            }
        }
    }
}
}