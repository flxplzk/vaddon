package de.flxplzk.vaadin.mvvm;

import com.vaadin.data.HasValue;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.UIDetachedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static de.flxplzk.vaadin.mvvm.ReflectionUtil.get;


/**
 * @author felix plazek
 */
class ItemWiring implements Wiring {

    /**
     * {@inheritDoc}
     *
     * Creates bindings if {@code ItemBound.class} annotation is present.
     *
     * @see ItemBound
     */
    @Override
    public List<Binding> wire(Object view, Object viewModel) {
        List<Binding> bindings = new ArrayList<>();
        for (Field viewField : view.getClass().getDeclaredFields()) {
            if (viewField.isAnnotationPresent(ItemBound.class)) {
                bindings.add(new ItemBinding(view, viewModel, viewField));
            }
        }
        return bindings;
    }

    /**
     * An Instance of this class links the annotated field in the view
     * component with the corresponding field in the viewModel.
     *
     * Both fields must be implementations of the HasValue interface.
     *
     * The binding instance registers itself as ValueChangeListener
     * in both fields. When receiving a ValueChangeEvent from one
     * of the fields the value is set to the other field.
     *
     * @author felix plazek
     *
     * @see HasValue
     * @see HasValue.ValueChangeListener
     */
    static class ItemBinding implements Binding, HasValue.ValueChangeListener{

        private static final Logger LOGGER = LoggerFactory.getLogger(ItemBinding.class);

        private List<Registration> registrations = new ArrayList<>();

        private final transient Object viewModel;
        private final transient Object view;
        private final transient Field viewerField;

        private HasValue valueProperty;
        private HasValue valueViewer;

        public ItemBinding(Object view, Object viewModel, Field viewerField) {
            this.viewerField = viewerField;
            this.view = view;
            this.viewModel = viewModel;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void valueChange(HasValue.ValueChangeEvent valueChangeEvent) {
            if (valueChangeEvent.getValue() == null) {
                valueProperty.setValue(valueProperty.getEmptyValue());
                return;
            }
            if (valueChangeEvent.getValue().equals(this.valueViewer.getValue())
                    && valueChangeEvent.getValue().equals(this.valueProperty.getValue())) {
                return;
            }
            if (valueChangeEvent.getSource() == valueProperty) {
                UI ui = ((Component) view).getUI();
                if (ui != null) {
                    try {
                        ui.access(() -> valueViewer.setValue(valueProperty.getValue()));
                    } catch (UIDetachedException uiDetachedException) {
                        LOGGER.debug("error accessing ui from background thread", uiDetachedException);
                    }
                } else {
                    valueViewer.setValue(valueProperty.getValue());
                }
            } else {
                valueProperty.setValue(valueViewer.getValue());
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void bind() {
            ItemBound itemBound = viewerField.getAnnotation(ItemBound.class);
            this.valueProperty = get(itemBound.to(), this.viewModel, HasValue.class);
            this.valueViewer = get(viewerField, view, HasValue.class);

            this.valueViewer.setValue(this.valueProperty.getValue());
            this.registrations.add(this.valueViewer.addValueChangeListener(this));
            this.registrations.add(this.valueProperty.addValueChangeListener(this));
        }

        @Override
        public void unbind() {
            this.registrations.forEach(Registration::remove);
            this.registrations = new ArrayList<>();
        }
    }
}
