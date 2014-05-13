package org.jetbrains.turb.tests

import org.jetbrains.turb.*
import org.spek.*

class ViewableSpecification : Spek() {{

    given("a viewable property") {
        val lifetime = LifetimeDefinition().lifetime
        val property = Property(2)
        on("observing filtered values") {
            var sum = 0
            property.filter { it % 2 == 0 }.view(lifetime) { (l,v)->
                sum += v
            }
            property.value = 3
            property.value = 1
            property.value = 4
            it("should sum even values to 6") {
                // 2 as initial value, and 4 as changed value
                shouldEqual(6, sum)
            }
        }
    }
}}