package org.jetbrains.turb.tests

import org.jetbrains.turb.*
import org.jetbrains.spek.api.*

class ViewableSpecification : Spek() {{

    given("a viewable property") {
        val scope = ScopeDefinition().scope
        val property = Property(scope, 2)
        on("observing filtered values") {
            var sum = 0
            property.filter { it % 2 == 0 }.view(scope) { (s,v)->
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

    given("a viewable property") {
        val scope = ScopeDefinition().scope
        val property = Property(scope, 0)
        on("observing mapped values") {
            var sum = ""
            property.map { it.toString()}.view(scope) { (s,v)->
                sum += v
            }
            property.value = 3
            property.value = 1
            property.value = 4
            it("should concat string to 0314") {
                shouldEqual("0314", sum)
            }
        }
    }
}}