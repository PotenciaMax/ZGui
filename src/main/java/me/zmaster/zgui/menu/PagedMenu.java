package me.zmaster.zgui.menu;

import me.zmaster.zgui.icon.Icon;
import me.zmaster.zgui.icon.PagedIconUpdater;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PagedMenu<I extends Icon> extends AbstractMenu {

    private final List<I> pagedIcons = new ArrayList<>();
    private final PagedIconUpdater pageUpdater = new PagedIconUpdater(this);
    private Comparator<I> iconComparator;

    public List<I> getPagedIcons() {
        if (iconComparator != null) {
            pagedIcons.sort(iconComparator);
        }
        return pagedIcons;
    }

    public void setIconComparator(Comparator<I> iconComparator) {
        this.iconComparator = iconComparator;
    }

    public PagedMenu(MenuMetadata metadata, Menu previousMenu) {
        super(metadata, previousMenu);
    }
}
