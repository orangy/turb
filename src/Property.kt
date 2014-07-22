package org.jetbrains.turb

public class Property<TValue>(val scope: Scope, initial: TValue) : Viewable<TValue> {
    {
        scope.attach {
            leave(currentValue)
            valueScope.dispose()
        }
    }

    var currentValue = initial
    var valueScope = ScopeDefinition()

    public var value: TValue
        get() = currentValue
        set(new: TValue) {
            if (value == new)
                return
            leave(value)
            valueScope.dispose()
            currentValue = new
            valueScope = ScopeDefinition()
            enter(value)
        }

    public val leave: Signal<TValue> = Signal {(l, f) -> }
    public val enter: Signal<TValue> = Signal {(l, f) -> f(currentValue) }

    override fun view(scope: Scope, action: (Scope, TValue) -> Unit) {
        enter.attach(scope) {
            action(valueScope.scope, currentValue)
        }
    }
}