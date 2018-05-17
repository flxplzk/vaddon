package de.flxplzk.vaddon.binding;

import com.vaadin.data.HasValue;
import com.vaadin.shared.Registration;
import com.vaadin.ui.CustomComponent;

/**
 * @author felix plazek
 * @param <T> valueType
 */
public abstract class HasValueComponent<T> extends CustomComponent implements HasValue<T> {

    protected final Property<T> valueProperty;

    public HasValueComponent(T value) {
        this.valueProperty = new Property<>(value);
    }

    @Override
    public void setValue(T value) {
        this.valueProperty.setValue(value);
        this.renderContent();
    }


    @Override
    public T getValue() {
        return this.valueProperty.getValue();
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        this.valueProperty.setRequiredIndicatorVisible(requiredIndicatorVisible);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return this.valueProperty.isRequiredIndicatorVisible();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.valueProperty.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return this.valueProperty.isReadOnly();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<T> listener) {
        return this.valueProperty.addValueChangeListener(listener);
    }

    protected abstract void renderContent();
}
