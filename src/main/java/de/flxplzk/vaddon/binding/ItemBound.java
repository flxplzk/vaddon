package de.flxplzk.vaddon.binding;

import com.vaadin.data.HasValue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation @ItemBound is applicable for every Vaadin
 * UI component that implements the HasValue interface.
 *
 * It binds the value to the corresponding property which
 * is defined by the parameter to().
 *
 * The target field must implement the HasValue interface!
 * The type parameter <bold>must</bold> match
 * the annotated field. You can use the generic implementation
 * of property.
 *
 * example the annotation in the view Component:
 * <pre>
 * <code>
 *  &#64;ItemBound(to = "firstName")
 *     private TextField firstName = new TextField("Your Name");
 * </code>
 * example field in the viewModel:
 * <code>
 * </code>
 *     private Property<String> firstName = new Property("Hans");
 * </code>
 * </pre>
 *
 * @author felix plazek
 *
 * @see HasValue
 * @see Property
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ItemBound {

    /**
     * That value defines the target field in the viewModel.
     * The name MUST match, otherwise the binding will fail.
     *
     * the target and the annotated field has to implement the
     * HasValue interface. The type parameter must match.
     *
     * @return name of the target field in the viewModel
     */
    String to();

}
