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

    public val changing: Signal<TValue> = Signal()
    public val changed: Signal<TValue> = Signal()

    override fun view(lifetime: Lifetime, action: (Lifetime, TValue) -> Unit) {
        action(valueLifetime.lifetime, currentValue)
        changed.attach(lifetime) {
            action(valueLifetime.lifetime, currentValue)
        }
    }
}