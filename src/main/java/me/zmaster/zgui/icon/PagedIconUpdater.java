package me.zmaster.zgui.icon;

import me.zmaster.zgui.menu.PagedMenu;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Map;

public class PagedIconUpdater extends AbstractIconUpdater {

    private final PagedMenu<?> menu;
    private final Map<Integer, Icon> icons;
    private final List<? extends Icon> pagedIcons;
    private final Inventory inventory;
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
                setIcon(slot, null);
                continue;
            }

            Icon pagedIcon = pagedIcons.get(position);
            setIcon(slot, pagedIcon);

            position++;
        }
    }

    public PagedIconUpdater(PagedMenu<?> menu) {
        this.menu = menu;
        this.icons = menu.getIcons();
        this.pagedIcons = menu.getPagedIcons();
        this.inventory = menu.getInventory();
    }

    private void setIcon(int slot, Icon pagedIcon) {
        if (pagedIcon == null) {
            icons.remove(slot);
            inventory.setItem(slot, null);
            return;
        }

        icons.put(slot, pagedIcon);
        inventory.setItem(slot, pagedIcon.getItem());
    }
}
