package de.flxplzk.vaddon.binding;

import com.vaadin.data.HasValue;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Grid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static de.flxplzk.vaddon.binding.ReflectionUtil.get;

public class SelectionWiring implements Wiring {

    @Override
    public List<Binding> wire(Object view, Object viewModel) {
        List<Binding> bindings = new ArrayList<>();
        for (Field viewField : view.getClass().getDeclaredFields()) {
            if (viewField.isAnnotationPresent(SelectionBound.class)) {
                bindings.add(new SelectionBinding(view, viewModel, viewField));
            }
        }
        return bindings;
    }


    @SuppressWarnings("unchecked")
    private class SelectionBinding implements Binding {

        private final Object view;
        private final Object viewModel;
        private final Field viewField;

        private Grid grid;
        private HasValue hasValue;
        private Registration registration;

        SelectionBinding(Object view, Object viewModel, Field viewField) {
            this.view = view;
            this.viewModel = viewModel;
            this.viewField = viewField;
        }

        @Override
        public void bind() {
            SelectionBound itemBound = viewField.getAnnotation(SelectionBound.class);
            this.hasValue = get(itemBound.to(), this.viewModel, HasValue.class);
            this.grid = get(viewField, view, Grid.class);

            registration = this.grid.addSelectionListener(this::itemClick);
            this.hasValue.addValueChangeListener(this::valueChange);
        }

        private void valueChange(HasValue.ValueChangeEvent valueChangeEvent) {
            if (this.grid.getSelectedItems().contains(valueChangeEvent.getValue())) {
                return;
            }
            this.grid.select(valueChangeEvent.getValue());
        }

        private void itemClick(SelectionEvent selectionEvent) {
            Optional firstSelectedItem = selectionEvent.getFirstSelectedItem();
            firstSelectedItem.ifPresent((s) -> this.hasValue.setValue(s));
        }


        @Override
        public void unbind() {
            if (this.registration != null) {
                this.registration.remove();
                this.registration = null;
            }
        }
    }
}
