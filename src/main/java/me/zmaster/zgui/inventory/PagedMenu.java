package me.zmaster.zgui.inventory;

import me.zmaster.zgui.Menu;
import me.zmaster.zgui.inventory.element.Element;
import me.zmaster.zgui.inventory.element.NextPageIcon;
import me.zmaster.zgui.inventory.element.view.ElementsView;
import me.zmaster.zgui.inventory.element.view.PagedElementsView;
import me.zmaster.zgui.inventory.meta.MenuMeta;

/**
 * A menu implementation that supports pagination of icons.
 * <p>
 * This menu manages a list of paged icons and provides
 * functionality to navigate between pages using next and previous page icons.
 * It uses separate updaters to refresh navigation icons and paged icons specifically.
 */
public class PagedMenu<E extends Element> extends AbstractMenu {

    private final PagedElementsView<E> pageView = new PagedElementsView<>(this);
    private final ElementsView navigationView = new ElementsView(this);

    public PagedMenu(MenuMeta<?> menuMeta, Menu previousMenu) {
        super(menuMeta, previousMenu);

        navigationView.applyElement(menuMeta, "next_page", meta ->
                new NextPageIcon(meta, pageView, navigationView, NextPageIcon.NEXT_PAGE_DIRECTION));

        navigationView.applyElement(menuMeta, "previous_page", meta ->
                new NextPageIcon(meta, pageView, navigationView, NextPageIcon.PREVIOUS_PAGE_DIRECTION));
    }

    /**
     * Returns the view for navigation icons (next page, previous page).
     *
     * @return the {@code View} for navigation icons
     */
    public ElementsView getNavigationView() {
        return navigationView;
    }

    /**
     * Returns the view for the paged icons.
     *
     * @return the {@code PagedElementsView} for paged icons
     */
    public PagedElementsView<E> getPageView() {
        return pageView;
    }

    public void updateView() {
        getPageView().update();
        getNavigationView().update();
    }
}
