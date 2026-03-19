package me.zmaster.zgui.inventory;

import me.zmaster.zgui.Menu;
import me.zmaster.zgui.inventory.element.Element;
import me.zmaster.zgui.inventory.element.NextPageIcon;
import me.zmaster.zgui.inventory.element.view.ElementView;
import me.zmaster.zgui.inventory.element.view.PagedElementsView;
import me.zmaster.zgui.inventory.meta.ElementMeta;
import me.zmaster.zgui.inventory.meta.MenuMeta;
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
public class PagedMenu<T extends Element> extends AbstractMenu {

    private final ElementView navigationView = ElementView.create(this);
    private final PagedElementsView pageView = new PagedElementsView(this);
    private final List<T> pagedElements = new ArrayList<>();
    private Comparator<T> iconComparator;

    public PagedMenu(MenuMeta<?> menuMeta, Menu previousMenu) {
        super(menuMeta, previousMenu);
        pageView.addSlots(menuMeta.getPagedSlots());

        applyElement(menuMeta, "next_page", meta ->
                new NextPageIcon(meta, this, NextPageIcon.NEXT_PAGE_DIRECTION), false);
        applyElement(menuMeta, "previous_page", meta ->
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
    public List<T> getPagedElements() {
        return pagedElements;
    }

    public Comparator<T> getIconComparator() {
        return iconComparator;
    }

    /**
     * Sets the comparator to sort paged icons.
     *
     * @param iconComparator comparator to sort paged icons
     */
    public void setIconComparator(@Nullable Comparator<T> iconComparator) {
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
