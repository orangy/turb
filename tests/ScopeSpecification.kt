package org.jetbrains.turb.tests

import org.jetbrains.turb.*
import org.spek.*


class ScopeSpecification : Spek() {{

    given("Scope definition") {
        var value = 0
        val def = ScopeDefinition()
        on("disposing definition") {
            val scope = def.scope
            scope.attach { value++ }
            def.dispose()
            it("should invoke handler") {
                shouldEqual(1, value)
            }
        }
    }

    given("Nested scope") {

        on("disposing nested scope") {
            var value = 0
            val def = ScopeDefinition()
            val nested = def.scope.nested()
            nested.attach { value++ }
            nested.dispose()
            def.dispose()
            it("should invoke handler only once") {
                shouldEqual(1, value)
            }
        }

        on("disposing outer scope") {
            var value = 0
            val def = ScopeDefinition()
            val nested = def.scope.nested()
            nested.attach { value++ }
            def.dispose()
            it("should invoke handler only once") {
                shouldEqual(1, value)
            }
        }
    }

    given("Intersected sopecs") {
        on("disposing intersected scope") {
            var value = 0
            val def1 = ScopeDefinition()
            val def2 = ScopeDefinition()
            val intersected = def1.scope.intersect(def2.scope)
            intersected.attach { value++ }
            intersected.dispose()
            def1.dispose()
            def2.dispose()
            it("should invoke handler only once") {
                shouldEqual(1, value)
            }
        }
        on("disposing outer scope") {
            var value = 0
            val def1 = ScopeDefinition()
            val def2 = ScopeDefinition()
            val intersected = def1.scope.intersect(def2.scope)
            intersected += { value++ }
            def1.dispose()
            def2.dispose()
            it("should invoke handler only once") {
                shouldEqual(1, value)
            }
        }
    }

}}
