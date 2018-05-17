package de.flxplzk.vaddon.styling;

/**
 * This Annotation is used for styling of Vaadin Components. Most Vaadin Styleing code
 * is very unreadable and causes much boilerplate code looks like this:
 *
 * {@code
 *      Button button = new Button();
 *      button.setCaption("some caption");
 *      button.setSizeFull();
 *      button.addStyleName("some style name");
 *      ...
 * }
 *
 * With this annotation the same effect can be realized, with:
 *
 * {@code
 *      #64;Style(caption = "some caption", fullSize = true, styleNames = {"some style name"})
 *      Button button = new Button();
 * }
 *
 * @author Felix Plazek
 */
public @interface ComponentStyle {
    String caption() default "";
    boolean sizeFull() default false;
    boolean sizeUndefined() default true;
    String[] styleNames() default {};

}
