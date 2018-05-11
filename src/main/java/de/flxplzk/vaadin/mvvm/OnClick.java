package de.flxplzk.vaadin.mvvm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation @OnClick is applicable for any Vaadin Button
 * component.
 *
 * It binds the button to the corresponding method of the viewModel
 * which is defined by the parameter method(). The target method may
 * not accept any parameters. In case a method of the view shall be
 * triggered after the defined onClickMethod it can be provided as
 * callback parameter. The callback method may not accept any parameters
 *
 * example the annotation in the view Component:
 * <pre>
 * <code>
 *  &#64;ItemBound(to = "doSomething")
 *     private Button firstButton = new Button("Hit me");
 *
 *     &#64;ItemBound(to = "doSomethingDifferent", callback = "reset)
 *     private Button secondButton = new Button("One more time!");
 *
 *     private void reset(){
 *          // ...
 *     }
 * </code>
 * example code in the viewModel:
 * <code>
 * </code>
 *     private void doSomething(){
 *         // ...
 *     }
 *
 *     private void doSomethingDifferent(){
 *         // ...
 *     }
 * </code>
 * </pre>
 *
 * @author felix plazek
 *
 * @see com.vaadin.ui.Button
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OnClick {

    /**
     * provides the name of the target method in the viewModel instance
     * 
     * @return name of the method that shall be invoked on an OnCklickEvent
     */
    String method();

    /**
     * provides the name of the target method in the view instance that shall be invoked
     * if the execution of the OnClick method is finished.
     *
     * @return name of the method that shall be invoked in the view
     * after execution of the on click method
     */
    String callback() default "";

}
