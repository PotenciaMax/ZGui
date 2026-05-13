package me.zmaster.zgui.inventory;

import me.zmaster.zgui.Menu;
import me.zmaster.zgui.inventory.element.Element;
import me.zmaster.zgui.inventory.element.Icon;
import me.zmaster.zgui.inventory.element.abstraction.NextPageElement;
import me.zmaster.zgui.inventory.element.view.ElementsView;
import me.zmaster.zgui.inventory.element.view.SimpleElementsView;
import me.zmaster.zgui.inventory.element.view.SortedPagedIconsView;
import me.zmaster.zgui.inventory.meta.MenuMeta;

public abstract class AbstractPagedMenu<E extends Icon> extends AbstractMenu {

    private final SortedPagedIconsView<E> pagedView = new SortedPagedIconsView<>(this);
    private final ElementsView<Element> navigationView = new SimpleElementsView<>(this);

    public AbstractPagedMenu(MenuMeta<?> menuMeta, Menu previousMenu) {
        super(menuMeta, previousMenu);

        menuMeta.getElementMeta("next_page")
                .map(meta -> new NextPageElement(meta, NextPageElement.NEXT_PAGE_DIRECTION, pagedView, navigationView))
                .ifPresent(navigationView::addElement);

        menuMeta.getElementMeta("previous_page")
                .map(meta -> new NextPageElement(meta, NextPageElement.PREVIOUS_PAGE_DIRECTION, pagedView, navigationView))
                .ifPresent(navigationView::addElement);
    }

    /**
     * Returns the view for the paged icons.
     *
     * @return the {@code SimplePagedIconsView} for paged icons
     */
    public SortedPagedIconsView<E> getPagedView() {
        return pagedView;
    }

    /**
     * Returns the view for navigation icons (next page, previous page).
     *
     * @return the {@code ElementsView} for navigation icons
     */
    public ElementsView<Element> getNavigationView() {
        return navigationView;
    }

    @Override
    protected void initialize() {
        super.initialize();

        pagedView.update();
        navigationView.update();
    }
}
