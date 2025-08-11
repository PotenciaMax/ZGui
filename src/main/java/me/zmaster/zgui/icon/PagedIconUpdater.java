package me.zmaster.zgui.icon;

import me.zmaster.zgui.menu.PagedMenu;

import java.util.List;

public class PagedIconUpdater extends AbstractIconUpdater {

    private final PagedMenu<?> menu;
    private final List<? extends Icon> pagedIcons;
    private int page = 1;

    @Override
    public PagedMenu<?> getMenu() {
        return menu;
    }

    public int getPage() {
        return page;
    }

    public int getLastPage() {
        return menu.getIcons().size() / getSlots().size();
    }

    public void setPage(int page) {
        if (page < 1) throw new IndexOutOfBoundsException("page cannot be < 1");
        this.page = page;
    }

    @Override
    public void update() {
        int pageSize = getSlots().size();
        if (pageSize == 0) {
            return;
        }

        int lastIconPos = pagedIcons.size() - 1;
        int startPos = pageSize * (page - 1);
        if (lastIconPos < startPos) {
            return;
        }

        int lastPos = Math.min(pageSize * page - 1, lastIconPos);
        int position = startPos;

        for (int slot : getSlots()) {
            if (position > lastPos) {
                menu.putIcon(slot, null, true);
                continue;
            }

            Icon pagedIcon = pagedIcons.get(position);
            menu.putIcon(slot, pagedIcon, true);

            position++;
        }
    }

    public PagedIconUpdater(PagedMenu<?> menu) {
        this.menu = menu;
        this.pagedIcons = menu.getPagedIcons();
    }

}
