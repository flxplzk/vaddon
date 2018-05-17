package de.flxplzk.vaddon.binding;

import java.util.List;

/**
 * this class handles the wiring of a object annotated with
 * &#64;View(model = ...) while wiring, every field is scanned
 * and if a field is annotated with with a specific Annotation
 * a new Binding is created and added to a list of bindings.
 *
 * @author felix plazek
 *
 * @see Binding
 * @see Wiring
 */
interface Wiring {
    /**
     * inspects all fields of the view instance. if a specific annotation
     * is present it creates a instance of Binding for that field.
     *
     * After inspecting all fields a List of all created bindings is returned.
     *
     * @param view the view instance
     * @param viewModel the corresponding viewModel instance
     * @return List of all created bindings
     */
    List<Binding> wire(Object view, Object viewModel);

    interface Binding {
        void bind();
        void unbind();
    }
}
