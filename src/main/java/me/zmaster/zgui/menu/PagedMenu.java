package me.zmaster.zgui.menu;

import me.zmaster.zgui.AbstractMenu;
import me.zmaster.zgui.icon.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PagedMenu<I extends Icon> extends AbstractMenu {

    private final List<I> pagedIcons = new ArrayList<>();
    private final IconUpdater navigationIconsUpdater = new IconUpdater(this);
    private final PagedIconUpdater pagedIconsUpdater = new PagedIconUpdater(this);
    private Comparator<I> iconComparator;

    /**
     * Returns the list of paged icons, sorted if a comparator is set.
     *
     * @return list of paged icons
     */
    public List<I> getPagedIcons() {
        if (iconComparator != null) {
            pagedIcons.sort(iconComparator);
        }
        return pagedIcons;
    }

    /**
     * Returns the updater for navigation icons (next page, previous page).
     *
     * @return the IconUpdater for navigation icons
     */
    public IconUpdater getNavigationIconsUpdater() {
        return navigationIconsUpdater;
    }

    /**
     * Returns the updater for the paged icons.
     *
     * @return the PagedIconUpdater for paged icons
     */
    public PagedIconUpdater getPagedIconsUpdater() {
        return pagedIconsUpdater;
    }

    /**
     * Sets the comparator to sort paged icons.
     *
     * @param iconComparator comparator to sort paged icons
     */
    public void setIconComparator(@Nullable Comparator<I> iconComparator) {
        this.iconComparator = iconComparator;
    }

    @IconHandler("next_page")
    public void nextPageIcon(IconMetadata meta) {
        putIcon(meta.getSlots(), new NextPageIcon(meta, this, NextPageIcon.NEXT_PAGE_DIRECTION));
    }

    @IconHandler("previous_page")
    public void previousPageIcon(IconMetadata meta) {
        putIcon(meta.getSlots(), new NextPageIcon(meta, this, NextPageIcon.PREVIOUS_PAGE_DIRECTION));
    }

    public PagedMenu(@NotNull MenuMetadata metadata, @Nullable Menu previousMenu) {
        super(metadata, previousMenu);
    }
}
