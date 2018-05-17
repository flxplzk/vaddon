package de.flxplzk.vaddon.common;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import de.flxplzk.vaddon.binding.ViewComponent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * If so, the WindowListener tries to read the corresponding Bean from the Application context.
 * If this operation was successful, the WindowListener opens a new ModalWindow in
 * the UI in which it is attached with the read viewComponent-Bean as content.
 *
 * @author felix plazek
 */
public class WindowManager {

    private final ApplicationContext applicationContext;
    private final UI ui;

    private ModalWindow currentWindow;

    public WindowManager(ApplicationContext applicationContext, UI ui) {
        this.applicationContext = applicationContext;
        this.ui = ui;
    }

    /**
     * The WindowListener tries to read the corresponding Bean from the Application context.
     * If this operation was successful, the WindowListener opens a new ModalWindow in
     * the UI in which it is attached with the read viewComponent-Bean as content.
     *
     * @param beanName
     *                  name of the ViewComponent-bean
     *
     * @throws WindowCreationException
     *                  if bean could not be retrieved from the application Context
     */
    public void addWindow(String beanName) {
        this.removeCurrentWindow();
        try {
            ViewComponent viewComponent = this.applicationContext.getBean(beanName, ViewComponent.class);
            this.currentWindow = new ModalWindow(viewComponent.getCaption(), viewComponent);
            this.ui.addWindow(this.currentWindow);
        } catch (BeansException e) {
            throw new WindowCreationException("unable to retrieve bean: " + beanName + " from applicationContext", e);
        }
    }

    /**
     * If a  window is attached, it will be removed.
     */
    public void removeCurrentWindow() {
        if (this.currentWindow != null) {
            this.currentWindow.close();
            this.currentWindow = null;
        }
    }

    /**
     * Template for attaching a new ModalWindow
     */
    public class ModalWindow extends Window {

        private final ViewComponent viewComponent;

        public ModalWindow(String caption, ViewComponent viewComponent) {
            super(caption, viewComponent);
            this.viewComponent = viewComponent;
            this.viewComponent.bind();
            setClosable(true);
            setModal(true);
            setResizable(false);
            setWidth(398, Unit.PIXELS);
            viewComponent.setWidth(398, Unit.PIXELS);
            addResizeListener(e -> center());
            addCloseListener(e -> removeCurrentWindow());
        }

        @Override
        public void close() {
            super.close();
            this.viewComponent.unbind();
        }
    }

    public static class WindowCreationException extends RuntimeException {

        WindowCreationException(String message, Throwable cause) {
            super(message, cause);
        }

    }
}
