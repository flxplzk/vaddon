package de.flxplzk.vaadin.mvvm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * for every view that shall be bound to its viewModel
 * this annotation has to be user. the annotated type has to
 * extends{@code Component}.
 *
 * <pre>
 *     {@code @View(model"nameOfTheViewModel)
 *       class myViewComponent implements Component{...}}
 * </pre>
 *
 * @author felix plazek
 *
 *  @see com.vaadin.ui.Component
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface View {
    String model();
}
