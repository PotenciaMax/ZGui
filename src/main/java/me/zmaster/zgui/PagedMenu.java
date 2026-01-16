package me.zmaster.zgui;

import me.zmaster.zgui.element.Element;
import me.zmaster.zgui.element.*;
import me.zmaster.zgui.element.view.ElementView;
import me.zmaster.zgui.element.view.PagedElementsView;
import me.zmaster.zgui.meta.MenuMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A menu implementation that supports pagination of icons.
 * <p>
 * This menu manages a list of paged icons and provides
 * functionality to navigate between pages using next and previous page icons.
 * It uses separate updaters to refresh navigation icons and paged icons specifically.
 */
public class PagedMenu<E extends Element> extends AbstractMenu {

    private final ElementView navigationView = ElementView.create(this);
    private final PagedElementsView pageView = new PagedElementsView(this);
    private final List<E> pagedElements = new ArrayList<>();
    private Comparator<E> iconComparator;

    public PagedMenu(MenuMeta menuMeta, Menu previousMenu) {
        super(menuMeta, previousMenu);
        pageView.addSlots(menuMeta.getPagedSlots());

        applyElement("next_page", meta ->
                new NextPageIcon(meta, this, NextPageIcon.NEXT_PAGE_DIRECTION), false);
        applyElement("previous_page", meta ->
                new NextPageIcon(meta, this, NextPageIcon.PREVIOUS_PAGE_DIRECTION), false);
    }

    /**
     * Returns the view for navigation icons (next page, previous page).
     *
     * @return the {@code ElementView} for navigation icons
     */
    public ElementView getNavigationView() {
        return navigationView;
    }

    /**
     * Returns the view for the paged icons.
     *
     * @return the {@code PagedElementsView} for paged icons
     */
    public PagedElementsView getPageView() {
        return pageView;
    }

    /**
     * Returns the list of paged icons, sorted if a comparator is set.
     *
     * @return list of paged icons
     */
    public List<E> getPagedElements() {
        return pagedElements;
    }

    public Comparator<E> getIconComparator() {
        return iconComparator;
    }

    /**
     * Sets the comparator to sort paged icons.
     *
     * @param iconComparator comparator to sort paged icons
     */
    public void setIconComparator(@Nullable Comparator<E> iconComparator) {
        this.iconComparator = iconComparator;
    }

    public void sortIcons() {
        if (iconComparator != null) {
            pagedElements.sort(iconComparator);
        }
    }

    public void updateView() {
        getPageView().update();
        getNavigationView().update();
    }
}
