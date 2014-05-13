package org.jetbrains.turb.tests

import org.jetbrains.turb.*
import org.spek.*


class LifetimeSpecification : Spek() {{

    given("Lifetime definition") {
        var value = 0
        val def = LifetimeDefinition()
        on("disposing definition") {
            val lifetime = def.lifetime
            lifetime.attach { value++ }
            def.dispose()
            it("should invoke handler") {
                shouldEqual(1, value)
            }
        }
    }

    given("Nested lifetime") {

        on("disposing nested lifetime") {
            var value = 0
            val def = LifetimeDefinition()
            val nested = def.lifetime.nested()
            nested.attach { value++ }
            nested.dispose()
            def.dispose()
            it("should invoke handler only once") {
                shouldEqual(1, value)
            }
        }

        on("disposing outer lifetime") {
            var value = 0
            val def = LifetimeDefinition()
            val nested = def.lifetime.nested()
            nested.attach { value++ }
            def.dispose()
            it("should invoke handler only once") {
                shouldEqual(1, value)
            }
        }
    }

    given("Intersected lifetimes") {
        on("disposing intersected lifetime") {
            var value = 0
            val def1 = LifetimeDefinition()
            val def2 = LifetimeDefinition()
            val intersected = def1.lifetime.intersect(def2.lifetime)
            intersected.attach { value++ }
            intersected.dispose()
            def1.dispose()
            def2.dispose()
            it("should invoke handler only once") {
                shouldEqual(1, value)
            }
        }
        on("disposing outer lifetime") {
            var value = 0
            val def1 = LifetimeDefinition()
            val def2 = LifetimeDefinition()
            val intersected = def1.lifetime.intersect(def2.lifetime)
            intersected += { value++ }
            def1.dispose()
            def2.dispose()
            it("should invoke handler only once") {
                shouldEqual(1, value)
            }
        }
    }

}}
