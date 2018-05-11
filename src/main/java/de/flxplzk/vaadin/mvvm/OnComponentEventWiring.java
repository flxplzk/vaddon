package de.flxplzk.vaadin.mvvm;

import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class OnComponentEventWiring implements Wiring {

    /**
     * {@inheritDoc}
     *
     * Creates bindings if {@code OnComponentEvent.class} annotation is present.
     *
     * @see OnComponentEvent
     */
    @Override
    public List<Binding> wire(Object view, Object viewModel) {
        List<Binding> bindings = new ArrayList<>();
        for (Field viewField : view.getClass().getDeclaredFields()) {
            if (viewField.isAnnotationPresent(OnComponentEvent.class)) {
                bindings.add(new OnComponentEventBinding(view, viewModel, viewField));
            }
        }
        return bindings;
    }

    /**
     * This binding routes a received Component.Event the the target method of the
     * viewModel. the target method has to accept type of annotated {@code ofType()}
     * in {@code OnComponentEvent} annotation.
     *
     * @author felix plazek
     *
     * @see OnComponentEvent
     * @see Component.Event
     */
    static class OnComponentEventBinding implements Binding, Component.Listener {

        private final transient Object view;
        private final transient Object viewModel;
        private final transient Field viewField;
        private Class<? extends Component.Event> eventType;

        private transient Method invokeOnEvent;

        private Component component;
        private Registration registration;

        public OnComponentEventBinding(Object view, Object viewModel, Field viewField) {
            this.view = view;
            this.viewModel = viewModel;
            this.viewField = viewField;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void bind() {
            OnComponentEvent onComponentEventAnnotation = viewField.getDeclaredAnnotation(OnComponentEvent.class);
            this.eventType = onComponentEventAnnotation.ofType();
            this.component = ReflectionUtil.get(viewField, view, Component.class);
            this.invokeOnEvent = ReflectionUtil.getMethod(onComponentEventAnnotation.method(), this.viewModel, eventType);
            this.registration = this.component.addListener(this);

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void unbind() {
            if (this.registration != null){
                this.registration.remove();
                this.registration = null;
                this.component = null;
            }
        }

        /**
         * {@inheritDoc}
         *
         * @param event event that is accepted as parameter
         *              by the target method of this binding
         */
        @Override
        public void componentEvent(Component.Event event) {
            if (this.eventType.equals(event.getClass())){
                try {
                    this.invokeOnEvent.invoke(this.viewModel, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new BindingException("unable to invoke method: '"
                            + this.invokeOnEvent.getName()
                            + "' with argument of type:" + event.getClass().getName(),
                            e);
                }
            }
        }
    }
}
