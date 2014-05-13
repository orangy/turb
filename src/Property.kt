package org.jetbrains.turb

public class Property<TValue>(val lifetime : Lifetime, initial: TValue) : Viewable<TValue> {
    {
        lifetime.attach {
            leave(currentValue)
            valueLifetime.dispose()
        }
    }

    var currentValue = initial
    var valueLifetime = LifetimeDefinition()

    public var value: TValue
        get() = currentValue
        set(new: TValue) {
            if (value == new)
                return
            leave(value)
            valueLifetime.dispose()
            currentValue = new
            valueLifetime = LifetimeDefinition()
            enter(value)
        }

    public val leave: Signal<TValue> = Signal { (l,f)-> }
    public val enter: Signal<TValue> = Signal { (l,f)-> f(currentValue)}

    override fun view(lifetime: Lifetime, action: (Lifetime, TValue) -> Unit) {
        enter.attach(lifetime) {
            action(valueLifetime.lifetime, currentValue)
        }
    }
}