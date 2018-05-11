package de.flxplzk.vaadin.viewcomponent;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;

/**
 * this interface aims to wrap view and component interface into one instance
 *
 * @author felix plazek
 */
public interface ViewComponent extends Component, View {

    void bind();
    void unbind();
}
