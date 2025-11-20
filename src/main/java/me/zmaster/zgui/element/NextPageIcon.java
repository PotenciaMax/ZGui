package me.zmaster.zgui.element;

import me.zmaster.zgui.element.view.PagedElementsView;
import me.zmaster.zgui.PagedMenu;
import me.zmaster.zgui.meta.ElementMeta;
import me.zmaster.zgui.util.Formater;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a navigation icon used in paged menus to move between pages.
 * Handles displaying the icon item based on whether the next/previous page
 * is available and processes click events to update the current page.
 */
public class NextPageIcon implements Icon {

    public static final int NEXT_PAGE_DIRECTION = 1;
    public static final int PREVIOUS_PAGE_DIRECTION = -1;

    private final ElementMeta meta;
    private final PagedMenu<?> menu;
    private final int pageDirection;

    public NextPageIcon(ElementMeta meta, PagedMenu<?> menu, int pageDirection) {
        if (pageDirection == 0) throw new ArithmeticException("pageDirection cannot be 0");

        this.meta = meta;
        this.menu = menu;
        this.pageDirection = pageDirection;

        menu.getNavigationView().addSlots(meta.getSlots());
    }

    @Override
    public ItemStack getItem() {
        int nextPage = calculateNextPage();

        if (nextPage == 0) {
            return meta.getItem("not_next");
        }

        PagedElementsView pageView = menu.getPageView();
        Formater formater = new Formater(pageView.getPage(), pageView.getLastPage());
        return formater.formatItem(meta.getDefaultItem());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        int nextPage = calculateNextPage();
        if (nextPage == 0) return;

        PagedElementsView pageView = menu.getPageView();
        pageView.setPage(nextPage);
        pageView.update();
        menu.getNavigationView().update();
    }

    /**
     * Calculates the page number this icon should navigate to.
     * Returns 0 if there is no valid next/previous page.
     */
    private int calculateNextPage() {
        PagedElementsView pageView = menu.getPageView();
        int nextPage = pageView.getPage() + pageDirection;

        if (nextPage < 1 || nextPage > pageView.getLastPage()) {
            return 0;
        }

        return nextPage;
    }
}
