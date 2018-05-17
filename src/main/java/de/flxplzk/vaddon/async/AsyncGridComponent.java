package de.flxplzk.vaddon.async;

import com.google.common.collect.Lists;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import de.flxplzk.vaddon.binding.HasValueComponent;

import java.util.List;

/**
 * CustomComponent that show a progress indicator, showContent is called with false.
 *
 * @author felix plazek
 */
public class AsyncGridComponent<T> extends HasValueComponent<List<T>> {

    // ################################# UI COMPONENTS #################################

    private final Grid<T> mGrid = new Grid<>();
    private final ProgressBar mProgressBar = new ProgressBar();

    // ################################# UI STRUCTURE #################################

    private final HorizontalLayout mLayout = new HorizontalLayout(
            mGrid,
            mProgressBar
    );

    private final VerticalLayout rootLayout = new VerticalLayout(
            this.mLayout
    );

    public AsyncGridComponent() {
        super(Lists.newArrayList());
        this.setCompositionRoot(this.rootLayout);
        this.setSizeFull();
        this.buildComponent();
        showContent(false);
    }

    public Grid<T> getGrid() {
        return this.mGrid;
    }

    private void initProgressBar() {
        this.mProgressBar.setIndeterminate(true);
        this.mProgressBar.setSizeFull();
    }

    private void initGrid() {
        this.mGrid.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        this.mGrid.setHeight(100.0f, Sizeable.Unit.PERCENTAGE);
    }

    private void buildComponent() {
        initGrid();
        initProgressBar();
        this.rootLayout.setMargin(false);
        this.rootLayout.setSizeFull();
        this.mLayout.setSizeFull();
        this.mLayout.setComponentAlignment(this.mProgressBar, Alignment.TOP_CENTER);
        this.rootLayout.setExpandRatio(mLayout, 1.0f);
    }

    public void showContent(boolean showContent) {
        this.mProgressBar.setVisible(!showContent);
        this.mGrid.setVisible(showContent);
    }

    @Override
    protected void renderContent() {
        this.showContent(true);
        this.getGrid().setItems(this.valueProperty.getValue());
    }
}