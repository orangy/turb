package org.jetbrains.turb

public class Property<TValue>(initial: TValue) : Viewable<TValue> {
    var currentValue = initial
    var valueLifetime = LifetimeDefinition()

    public var value: TValue
        get() = currentValue
        set(new: TValue) {
            if (value == new)
                return
            changing(value)
            valueLifetime.dispose()
            currentValue = new
            valueLifetime = LifetimeDefinition()
            changed(value)
        }

    public val changing: Signal<TValue> = Signal { (l,f)-> }
    public val changed: Signal<TValue> = Signal { (l,f)-> f(currentValue)}

    override fun view(lifetime: Lifetime, action: (Lifetime, TValue) -> Unit) {
        changed.attach(lifetime) {
            action(valueLifetime.lifetime, currentValue)
        }
    }
}