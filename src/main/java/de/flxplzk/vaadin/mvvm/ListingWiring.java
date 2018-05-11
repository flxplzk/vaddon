package de.flxplzk.vaadin.mvvm;

import com.vaadin.data.HasItems;
import com.vaadin.data.HasValue;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class ListingWiring implements Wiring {

    /**
     * {@inheritDoc}
     * <p>
     * Creates bindings if {@code ListingBound.class} annotation is present.
     *
     * @see ListingBound
     */
    @Override
    public List<Binding> wire(Object view, Object viewModel) {
        List<Binding> bindings = new ArrayList<>();
        for (Field viewField : view.getClass().getDeclaredFields()) {
            if (viewField.isAnnotationPresent(ListingBound.class)) {
                bindings.add(new ListingBinding(view, viewModel, viewField));
            }
        }
        return bindings;
    }

    /**
     * An instance of this class links the annotated field in the view
     * component with the corresponding field in the viewModel.
     * <p>
     * The annotated field must be implementation of the HasItems interface.
     * The target field in the viewModel must be an implementation of the
     * HasValue interface.
     * <p>
     * The binding instance registers itself as ValueChangeListener
     * in both fields. When receiving a ValueChangeEvent from the target
     * field the value of the target field is set to the viewField.
     *
     * @author felix plazek
     * @see HasItems
     * @see HasValue
     */
    @SuppressWarnings("unchecked")
    static class ListingBinding implements Binding, HasValue.ValueChangeListener {

        private final transient Object view;
        private final transient Object viewModel;
        private final transient Field viewField;

        private HasItems viewer;
        private HasValue valueProperty;

        private Registration registration;

        public ListingBinding(Object view, Object viewModel, Field viewField) {
            this.view = view;
            this.viewModel = viewModel;
            this.viewField = viewField;
        }

        @Override
        public void bind() {
            ListingBound listingBound = viewField.getDeclaredAnnotation(ListingBound.class);
            this.valueProperty = ReflectionUtil.get(listingBound.to(), this.viewModel, HasValue.class);
            this.viewer = ReflectionUtil.get(this.viewField, this.view, HasItems.class);
            this.viewer.setItems(((List) this.valueProperty.getValue()).stream());
            this.registration = this.valueProperty.addValueChangeListener(this);
        }

        @Override
        public void unbind() {
            if (this.registration != null) {
                this.registration.remove();
                this.registration = null;
            }
        }

        @Override
        public void valueChange(HasValue.ValueChangeEvent event) {
            UI ui = ((Component) this.view).getUI();
            if (ui != null) {
                ui.access(() -> this.viewer.setItems((List) valueProperty.getValue()));
            } else {
                this.viewer.setItems((List) valueProperty.getValue());
            }
        }
    }
}
