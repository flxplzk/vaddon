package de.flxplzk.vaadin.mvvm;

import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class OnClickWiring implements Wiring {

    /**
     * {@inheritDoc}
     *
     * Creates bindings if {@code OnClick.class} annotation is present.
     *
     * @see OnClick
     */
    @Override
    public List<Binding> wire(Object view, Object viewModel) {
        List<Binding> bindings = new ArrayList<>();
        for (Field viewField : view.getClass().getDeclaredFields()) {
            if (viewField.isAnnotationPresent(OnClick.class)) {
                bindings.add(new OnClickBinding(view, viewModel, viewField));
            }
        }
        return bindings;
    }

    /**
     * An instance of this class links the annotated button in the view
     * component with the corresponding method in the viewModel.
     *
     * @author felix plazek
     */
    static class OnClickBinding implements Binding, Button.ClickListener {

        private final transient Object view;
        private final transient Object viewModel;
        private final transient Field viewField;

        private transient Method onClick;
        private transient Method callback;

        private Registration registration;

        OnClickBinding(Object view, Object viewModel, Field viewField) {
            this.view = view;
            this.viewModel = viewModel;
            this.viewField = viewField;
        }

        @Override
        public void bind() {
            OnClick onCLickAnnotation = viewField.getDeclaredAnnotation(OnClick.class);
            Button button = ReflectionUtil.get(viewField, view, Button.class);
            this.onClick = ReflectionUtil.getMethod(onCLickAnnotation.method(), this.viewModel);
            if (!"".equals(onCLickAnnotation.callback())) {
                this.callback = ReflectionUtil.getMethod(onCLickAnnotation.callback(), view);
            }
            this.registration = button.addClickListener(this);
        }

        @Override
        public void unbind() {
            if (this.registration != null) {
                this.registration.remove();
            }
        }

        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            try {
                this.onClick.invoke(viewModel);
                if (this.callback != null) {
                    this.callback.invoke(view);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new BindingException("unable to invoke method :" + this.onClick.getName(), e);
            }
        }
    }
}
