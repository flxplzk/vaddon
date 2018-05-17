package de.flxplzk.vaddon.binding;

import com.vaadin.data.HasValue;
import com.vaadin.shared.Registration;

import java.util.ArrayList;
import java.util.List;


/**
 * Implementation of the HasValue interface in order to use the binding annotation in viewModel
 * instances.
 *
 * @param <V> the value type
 *
 * @see HasValue
 */
public class Property<V> implements HasValue<V> {

    private transient V value;

    private final List<ValueChangeListener> listeners = new ArrayList<>();

    private boolean readOnly = false;
    private boolean isRequired = false;

    public Property(V value) {
        this.value = value;
    }

    /**
     * Sets the new value to the value field. If the property is for readOnlyUse
     * it throws a ReadOnlyException. After setting the value every registered
     * ValueChangeListener is notified about the value change.
     *
     * @param value the new value.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setValue(V value) {
        if (readOnly) {
            throw new ReadOnlyException("Cannot set a value to a readOnly property");
        }
        if (this.value == null) {
            this.value = value;
            ValueChangeEvent<V> valueChangeEvent = new ValueChangeEvent<>(null, this, null, false);
            this.listeners.forEach(listener -> listener.valueChange(valueChangeEvent));
        } else {
            V oldValue = this.value;
            this.value = value;
            ValueChangeEvent<V> valueChangeEvent = new ValueChangeEvent<>(null, this, oldValue, false);
            this.listeners.forEach(listener -> listener.valueChange(valueChangeEvent));
        }
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<V> valueChangeListener) {
        this.listeners.add(valueChangeListener);
        return () -> listeners.remove(valueChangeListener);
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        this.isRequired = requiredIndicatorVisible;
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return this.isRequired;
    }

    @Override
    public void setReadOnly(boolean newValue) {
        this.readOnly = newValue;
    }

    @Override
    public boolean isReadOnly() {
        return this.readOnly;
    }
}
