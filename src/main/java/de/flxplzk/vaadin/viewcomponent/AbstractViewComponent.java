package de.flxplzk.vaadin.viewcomponent;

import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.Registration;
import com.vaadin.ui.CustomComponent;
import de.flxplzk.vaadin.mvvm.ViewModelComposer;
import de.flxplzk.vaadin.viewcomponent.ViewComponent;

/**
 * @author felix plazek
 */
public class AbstractViewComponent extends CustomComponent implements ViewComponent {

    private final transient ViewModelComposer viewModelComposer;
    private Registration viewModelRegistration;

    public AbstractViewComponent(ViewModelComposer viewModelComposer) {
        this.viewModelComposer = viewModelComposer;
    }

    /**
     * this method is called by navigator before the view is displayed.
     * Data initializing and binding should take play in this method.
     *
     * @param event this Event is provided by the navigator
     *              before the view is displayed on screen.
     */
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.bind();
    }

    /**
     * this method is called by navigator before the view is displayed.
     * Data initializing and unbinding should take play in this method.
     *
     * @param event 's method <code>navigate()</code> must be called
     *              otherwise the user will NOT be able to change the view.
     */
    @Override
    public void beforeLeave(ViewBeforeLeaveEvent event) {
        this.unbind();
        event.navigate();
    }

    @Override
    public void bind() {
        viewModelRegistration = viewModelComposer.bind(this);
    }

    @Override
    public void unbind() {
        if (this.viewModelRegistration != null) {
            viewModelRegistration.remove();
            viewModelRegistration = null;
        }
    }
}
