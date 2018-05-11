package de.flxplzk.vaadin.mvvm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation @Listing is applicable for every Vaadin
 * UI component that implements the HasItems interface.
 *
 * It binds the value to the corresponding property which
 * name is defined by the parameter to().
 *
 * The target field must implement the HasValue of type
 * {@code List<YourType>} interface!
 *
 * The type parameter <bold>must</bold> match
 * the annotated field. You can use the generic implementation
 * of property.
 *
 * example the annotation in the view Component:
 * <pre>
 * {@code @ListingBound(to = "firstNames")
 *   private Grid<String> firstName = new Grid<>();
 *   }
 * {@code @ListingBound(to = "firstNames")
 *   private ComboBox<String> firstName = new ComboBox<>();
 * }
 * </pre>
 * example field in the viewModel:
 * <pre>
 * {@code private Property<List<String>> firstNames = new Property(new ArrayList<>());
 * }
 * </pre>
 * @author felix plazek
 *
 * @see com.vaadin.data.HasValue
 * @see com.vaadin.data.HasItems
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ListingBound {

    /**
     * Required parameter for ListingBound annotation
     *
     * @return
     * name of the target field in the viewModel.
     */
    String to();

}
