package de.flxplzk.vaddon.binding;

import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModelComposer composes the binding process of a view with the
 * annotated viewModel. The Annotated View must extend the Component
 * interface.
 *
 * @author felix plazek
 * @see Component
 */
public class ViewModelComposer {

    private long nextVal = 0;

    private final Wiring[] wirings = {
            new ItemWiring()
            , new ListingWiring()
            , new OnClickWiring()
            , new OnComponentEventWiring()
            , new EnableBoundWiring()
            , new SelectionWiring()
    };
    private final ApplicationContext applicationContext;
    private final Map<Long, List<Wiring.Binding>> bindings = new HashMap<>();

    public ViewModelComposer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * This method handles the binding to the viewModelBean. The name of the
     * bean has to match the Value provided in the
     * {@code @View(model = "nameOfViewModelBean") }
     * annotation. If the name does not match a BeansException will be thrown. If
     * the {@code @View} Annotation is missing, a BindingException will be thrown.
     * <p>
     * <pre>
     *     example code:
     *
     *     {@code @View(model = "myViewModel")
     *       public class myView extends CustomComponent {
     *           ...
     *       }}
     * </pre>
     *
     * @param view the view that shall be bound to the view model
     * @return Registration for the created binding;
     * the registration has to be used to unbind the view
     * with the viewModel. use: {@code Registration::remove}
     * @throws BindingException if {@code @View } annotation is missing,
     *                          if the given view model bean name is not correct
     *                          or any of the annotated view fields is not annotated correctly
     * @see Registration
     */
    public Registration bind(Component view) {
        final long keyForCurrentBinding = nextVal();

        if (!view.getClass().isAnnotationPresent(View.class)) {
            throw new BindingException("Annotation '@View' is missing; cannot bind to view viewModel.");
        }
        if (this.bindings.get(keyForCurrentBinding) == null) {
            bindings.put(keyForCurrentBinding, new ArrayList<>());
        }

        View viewAnnotation = view.getClass().getAnnotation(View.class);
        Object viewModel = this.applicationContext.getBean(viewAnnotation.model());

        for (Wiring wiring : this.wirings) {
            bindings.get(keyForCurrentBinding).addAll(wiring.wire(view, viewModel));
        }

        this.bindings.get(keyForCurrentBinding).forEach(Wiring.Binding::bind);

        return () -> {
            this.bindings.get(keyForCurrentBinding).forEach(Wiring.Binding::unbind);
            this.bindings.remove(keyForCurrentBinding);
        };
    }

    private long nextVal() {
        return this.nextVal++;
    }
}
