package me.zmaster.zgui.inventory.element.abstraction;

import me.zmaster.zgui.inventory.context.ClickContext;
import me.zmaster.zgui.inventory.element.Element;
import me.zmaster.zgui.inventory.element.view.ElementsView;
import me.zmaster.zgui.inventory.element.view.PagedIconsView;
import me.zmaster.zgui.inventory.meta.ElementMeta;
import me.zmaster.zgui.inventory.util.Formatter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a navigation icon used in paged menus to move between pages.
 * Handles displaying the icon item based on whether the next/previous page
 * is available and processes click events to update the current page.
 */
public class NextPageElement extends AbstractElement {

    public static final int NEXT_PAGE_DIRECTION = 1;
    public static final int PREVIOUS_PAGE_DIRECTION = -1;

    private final ElementMeta meta;
    private final PagedIconsView<?> pageView;
    private final ElementsView<?> navigationView;
    private final int pageDirection;

    public NextPageElement(@NotNull ElementMeta meta, int pageDirection, @NotNull PagedIconsView<?> pageView, @NotNull ElementsView<?> navigationView) {
        super(meta);

        if (pageDirection == 0) throw new IllegalArgumentException("pageDirection cannot be 0");

        this.meta = Objects.requireNonNull(meta);
        this.pageDirection = pageDirection;
        this.pageView = Objects.requireNonNull(pageView);
        this.navigationView = Objects.requireNonNull(navigationView);
    }

    @Override
    public ItemStack getItem() {
        int nextPage = calculateNextPage();

        if (nextPage == 0) {
            return meta.getItem("not_next");
        }

        Formatter formatter = new Formatter.Builder()
                .add("current_page", pageView.getPage())
                .add("last_page", pageView.getLastPage())
                .build();

        return formatter.format(meta.getDefaultItem());
    }

    @Override
    public void onClick(@NotNull ClickContext event) {
        int nextPage = calculateNextPage();
        if (nextPage == 0) return;

        pageView.setPage(nextPage);
        pageView.update();
        navigationView.update();
    }

    /**
     * Calculates the page number this icon should navigate to.
     * Returns 0 if there is no valid next/previous page.
     */
    private int calculateNextPage() {
        int nextPage = pageView.getPage() + pageDirection;

        if (nextPage < 1 || nextPage > pageView.getLastPage())
            return 0;

        return nextPage;
    }
}
