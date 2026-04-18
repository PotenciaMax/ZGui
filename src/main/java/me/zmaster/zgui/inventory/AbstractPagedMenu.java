package me.zmaster.zgui.inventory;

import me.zmaster.zgui.Menu;
import me.zmaster.zgui.inventory.element.Element;
import me.zmaster.zgui.inventory.element.Icon;
import me.zmaster.zgui.inventory.element.abstraction.NextPageElement;
import me.zmaster.zgui.inventory.element.view.ElementsView;
import me.zmaster.zgui.inventory.element.view.SimpleElementsView;
import me.zmaster.zgui.inventory.element.view.SortedPagedIconsView;
import me.zmaster.zgui.inventory.meta.MenuMeta;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * A menu implementation that supports pagination of icons.
 * <p>
 * This menu manages a list of paged icons and provides
 * functionality to navigate between pages using next and previous page icons.
 * It uses separate updaters to refresh navigation icons and paged icons specifically.
 */
public abstract class AbstractPagedMenu<E extends Icon> extends AbstractMenu {

    private final List<E> icons = new ArrayList<>();
    private final SortedPagedIconsView<E> pagedView = new SortedPagedIconsView<>(this);
    private final ElementsView<Element> navigationView = new SimpleElementsView<>(this);
    private Predicate<? extends E> filter;
    private Comparator<? extends E> comparator;

    public AbstractPagedMenu(MenuMeta<?> menuMeta, Menu previousMenu) {
        super(menuMeta, previousMenu);

        menuMeta.getElementMeta("next_page")
                .map(meta -> new NextPageElement(meta, pagedView, navigationView, NextPageElement.NEXT_PAGE_DIRECTION))
                .ifPresent(getNavigationView()::addElement);

        menuMeta.getElementMeta("previous_page")
                .map(meta -> new NextPageElement(meta, pagedView, navigationView, NextPageElement.PREVIOUS_PAGE_DIRECTION))
                .ifPresent(getNavigationView()::addElement);
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
        pagedView.update();
        navigationView.update();
    }
}
