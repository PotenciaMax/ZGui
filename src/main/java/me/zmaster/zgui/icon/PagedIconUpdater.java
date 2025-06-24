package me.zmaster.zgui.icon;

import me.zmaster.zgui.menu.PagedMenu;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Map;

public class PagedIconUpdater extends AbstractIconUpdater {

    private final PagedMenu<?> menu;
    private int page;

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
        if (page < 0) throw new IllegalStateException("page cannot be negative");
        this.page = Math.min(page, getLastPage());
    }

    @Override
    public void update() {
        int pageSize = getSlots().size();
        if (pageSize == 0) {
            return;
        }

        List<? extends Icon> pagedIcons = menu.getPagedIcons();

        int lastIconPos = pagedIcons.size() - 1;
        int startPos = pageSize * (page - 1);
        if (lastIconPos < startPos) {
            return;
        }

        Inventory inv = menu.getInventory();
        Map<Integer, Icon> icons = menu.getIcons();

        int lastPos = Math.min(pageSize * page - 1, lastIconPos);
        int position = startPos;

        for (int slot : getSlots()) {
            Icon pagedIcon = pagedIcons.get(startPos);
            icons.put(slot, pagedIcon);
            inv.setItem(slot, pagedIcon.getItem());

            if (position >= lastPos) {
                break;
            }

            position++;
        }
    }

    public PagedIconUpdater(PagedMenu<?> menu) {
        this.menu = menu;
    }
}
