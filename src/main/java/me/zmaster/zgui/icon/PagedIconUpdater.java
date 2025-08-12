package me.zmaster.zgui.icon;

import me.zmaster.zgui.menu.PagedMenu;

import java.util.List;

/**
 * Icon updater specialized for paged menus.
 * It manages the display of icons across multiple pages by updating
 * only the icons visible on the current page.
 */
public class PagedIconUpdater extends AbstractIconUpdater {

    private final PagedMenu<?> menu;
    private final List<? extends Icon> pagedIcons;
    private int page = 1;

    @Override
    public PagedMenu<?> getMenu() {
        return menu;
    }

    /**
     * Returns the current page number.
     */
    public int getPage() {
        return page;
    }

    /**
     * Calculates and returns the last page number based on the total icons and slots available.
     */
    public int getLastPage() {
        return menu.getIcons().size() / getSlots().size();
    }

    /**
     * Sets the current page number.
     * @param page the new page number, must be >= 1
     * @throws IndexOutOfBoundsException if page < 1
     */
    public void setPage(int page) {
        if (page < 1) throw new IndexOutOfBoundsException("page cannot be < 1");
        this.page = page;
    }

    /**
     * Updates the icons on the current page in the menu.
     */
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
