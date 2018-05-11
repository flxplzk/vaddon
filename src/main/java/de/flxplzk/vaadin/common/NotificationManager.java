package de.flxplzk.vaadin.common;

import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This class is a helper class for simplifying the usage of notification for user feedback.
 * The NotificationManager provides different possibilities for displaying a notification.
 *
 * @author Felix Plazek
 *
 * @see Notification
 */
public class NotificationManager {

    private final UI currentUI;

    public NotificationManager(UI currentUI) {
        this.currentUI = currentUI;
    }

    public void showNotification(String title, String message, final NotificationStyle style) {
        final Notification notification = buildNotification(title, message, style);
        this.attach(notification);
    }

    public void showNotification(final String title, final NotificationStyle style) {
        final Notification notification = buildNotification(title, style);
        this.attach(notification);
    }

    private void attach(final Notification notification) {
        this.currentUI.access(() -> notification.show(currentUI.getPage()));
    }

    private Notification buildNotification(String title, String message, NotificationStyle style) {
        final Notification notification = new Notification(
                title,
                message,
                Notification.Type.WARNING_MESSAGE
        );
        notification.setStyleName(style.getStyleName());
        notification.setPosition(Position.TOP_CENTER);
        return notification;
    }

    private Notification buildNotification(String title, NotificationStyle style) {
        final Notification notification = new Notification(
                title,
                Notification.Type.WARNING_MESSAGE
        );
        notification.setStyleName(style.getStyleName());
        notification.setPosition(Position.TOP_CENTER);
        return notification;
    }

    public enum NotificationStyle {

        SUCCESS(ValoTheme.NOTIFICATION_SUCCESS),
        ERROR(ValoTheme.NOTIFICATION_ERROR),
        CRITICAL_ERROR(ValoTheme.NOTIFICATION_CRITICAL_ERROR),
        FAILURE(ValoTheme.NOTIFICATION_FAILURE);

        private final String styleName;

        NotificationStyle(String styleName) {
            this.styleName = styleName;
        }

        public String getStyleName() {
            return this.styleName;
        }
    }
}
