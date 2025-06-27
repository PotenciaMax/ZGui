package me.zmaster.zgui.menu;

import me.zmaster.zgui.icon.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PagedMenu<I extends Icon> extends AbstractMenu {

    private final List<I> pagedIcons = new ArrayList<>();
    private final PagedIconUpdater pagedIconsUpdater = new PagedIconUpdater(this);
    private final IconUpdater nextPageIconsUpdater = new IconUpdater(this);
    private Comparator<I> iconComparator;

    public List<I> getPagedIcons() {
        if (iconComparator != null) {
            pagedIcons.sort(iconComparator);
        }
        return pagedIcons;
    }

    public PagedIconUpdater getPagedIconsUpdater() {
        return pagedIconsUpdater;
    }

    public IconUpdater getNextPageIconsUpdater() {
        return nextPageIconsUpdater;
    }

    public void setIconComparator(@Nullable Comparator<I> iconComparator) {
        this.iconComparator = iconComparator;
    }

    @IconHandler("next_page")
    public void nextPageIcon(IconMetadata meta) {
        putIcon(meta.getSlot(), new NextPageIcon(meta, this, NextPageIcon.NEXT_PAGE_DIRECTION));
    }

    @IconHandler("previous_page")
    public void previousPageIcon(IconMetadata meta) {
        putIcon(meta.getSlot(), new NextPageIcon(meta, this, NextPageIcon.PREVIOUS_PAGE_DIRECTION));
    }

    public PagedMenu(@NotNull MenuMetadata metadata, @Nullable Menu previousMenu) {
        super(metadata, previousMenu);
    }
}
