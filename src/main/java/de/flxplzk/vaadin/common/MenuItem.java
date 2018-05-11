package de.flxplzk.vaadin.common;

import com.vaadin.server.Resource;

import java.util.List;

/**
 * menuItem is used for building the menu.
 *
 * @author felix plazek u555994
 */
public class MenuItem {

    private final String caption;
    private final Resource icon;
    private final ActionType actionType;
    private final String viewName;
    private final List<MenuItem> children;


    public MenuItem(String caption, Resource icon, ActionType actionType, String viewName, List<MenuItem> children) {
        this.caption = caption;
        this.viewName = viewName;
        this.children = children;
        this.icon = icon;
        this.actionType = actionType;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public String getCaption() {
        return caption;
    }

    public Resource getIcon() {
        return icon;
    }

    public List<MenuItem> getChildren() {
        return children;
    }

    public String getViewName() {
        return viewName;
    }

    public enum ActionType {

        VIEW,
        WINDOW,
        VOID;

    }
}
