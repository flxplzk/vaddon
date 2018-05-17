package de.flxplzk.vaddon.binding;

import com.vaadin.ui.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 *
 * @author felix plazek
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OnComponentEvent {

    /**
     * Target Method must accept this type as param.
     *
     * @return event type that shall be handled by target method.
     */
    Class<? extends Component.Event> ofType();

    /**
     *  defines name of the target method that will be invoked if
     *  the annotated event fires event which matches the type of
     *  {@code ofType()}
     *
     *  @return name of the target method in the viewModel
     */
    String method();

}
