package de.flxplzk.vaddon.common;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Class for getting user conformation.
 *
 * @author felix plazek
 */
public class ConfirmationDialog extends Window {

    // #################################### UI components ####################################

    private final Label message = new Label();
    private final Button buttonOk = new Button("OK", VaadinIcons.CHECK_CIRCLE);
    private final Button buttonCancel = new Button("Cancel", VaadinIcons.CLOSE_CIRCLE);

    // #################################### UI components ####################################

    private final HorizontalLayout buttonsLayout = new HorizontalLayout(
            this.buttonOk,
            this.buttonCancel
    );

    private final HorizontalLayout buttonsPanel = new HorizontalLayout(
            this.buttonsLayout
    );

    private final VerticalLayout rootLayout = new VerticalLayout(
            this.message,
            this.buttonsPanel
    );

    public ConfirmationDialog() {
        super("Confirm ... ");
        setContent(this.rootLayout);
        setClosable(false);
        setResizable(false);
        setModal(true);

        this.buttonsPanel.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_RIGHT);
        this.buttonOk.setStyleName(ValoTheme.BUTTON_PRIMARY);
        this.message.addStyleName(ValoTheme.LABEL_H3);
    }

    /**
     * adds confirmation window to the ui and waits for user action.
     *
     * on user action the ConfirmationCallback will be notified
     *
     * @param message to be viewed to the user
     * @param ui currentUI
     * @param confirmationCallback to be notified
     */
    public void confirm(String message, UI ui, ConfirmationCallback confirmationCallback) {
        this.message.setValue(message);
        ui.access(() -> ui.addWindow(this));
        this.buttonOk.addClickListener(event -> {
            close();
            confirmationCallback.handleResponse(ConfirmationCallback.ResponseType.OK);
        });
        this.buttonCancel.addClickListener(event -> {
            close();
            confirmationCallback.handleResponse(ConfirmationCallback.ResponseType.CANCEL);
        });
    }

    /**
     * Interface for handling user confirmation response
     *
     * @author felix plazek
     */
    public interface ConfirmationCallback {

        void handleResponse(final ResponseType responseType);

        enum ResponseType {
            OK,
            CANCEL;
        }
    }
}
