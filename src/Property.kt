package org.jetbrains.turb

public class Property<TValue>(initial: TValue) : Viewable<TValue> {
    var storage = initial
    var valueLifetime = LifetimeDefinition()

    public var value: TValue
        get() = storage
        set(new: TValue) {
            if (value == new)
                return
            changing(value)
            valueLifetime.dispose()
            value = new
            valueLifetime = LifetimeDefinition()
            changed(value)
        }

    public val changing: Signal<TValue> = Signal()
    public val changed: Signal<TValue> = Signal()

    override fun view(lifetime: Lifetime, observer: (Lifetime, TValue) -> Unit) {
        changed.attach(lifetime) {
            observer(valueLifetime.lifetime, storage)
        }
    }
}