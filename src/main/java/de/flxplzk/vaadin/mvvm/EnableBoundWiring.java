package de.flxplzk.vaadin.mvvm;

import com.vaadin.data.HasValue;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static de.flxplzk.vaadin.mvvm.ReflectionUtil.get;

public class EnableBoundWiring implements Wiring {
    @Override
    public List<Binding> wire(Object view, Object viewModel) {
        List<Binding> bindings = new ArrayList<>();
        for (Field viewField : view.getClass().getDeclaredFields()) {
            if (viewField.isAnnotationPresent(EnableBound.class)) {
                bindings.add(new EnableBinding(view, viewModel, viewField));
            }
        }
        return bindings;
    }

    static class EnableBinding implements Binding, HasValue.ValueChangeListener<Boolean> {

        private final Object view;
        private final Object viewModel;
        private final Field viewField;

        private Button button;
        private HasValue<Boolean> isEnabled;
        private Registration registration;

        EnableBinding(Object view, Object viewModel, Field viewField) {
            this.view = view;
            this.viewModel = viewModel;
            this.viewField = viewField;
        }

        @Override
        public void bind() {
            EnableBound enableBound = viewField.getAnnotation(EnableBound.class);
            this.isEnabled = get(enableBound.to(), this.viewModel, HasValue.class);
            this.button = get(this.viewField, view, Button.class);

            this.button.setEnabled(this.isEnabled.getValue());
            this.registration = this.isEnabled.addValueChangeListener(this);
        }

        @Override
        public void unbind() {
            if (this.registration != null) {
                this.registration.remove();
                this.registration = null;
            }
        }

        @Override
        public void valueChange(HasValue.ValueChangeEvent<Boolean> valueChangeEvent) {
            boolean value = valueChangeEvent.getValue();
            ((Component) view).getUI().access(() ->  this.button.setEnabled(value));
        }
    }
}
