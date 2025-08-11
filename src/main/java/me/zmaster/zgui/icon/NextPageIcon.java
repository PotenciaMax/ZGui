package me.zmaster.zgui.icon;

import me.zmaster.zgui.menu.PagedMenu;
import me.zmaster.zgui.util.Formater;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class NextPageIcon implements Icon {

    public static int NEXT_PAGE_DIRECTION = 1;
    public static int PREVIOUS_PAGE_DIRECTION = -1;
    private final IconMetadata meta;
    private final PagedMenu<?> menu;
    private final int pageDirection;
    private int nextPage;

    @Override
    public ItemStack getItem() {
        if (nextPage == 0) {
            return meta.getItemOrDefault("not_next");
        }

        ItemStack item = meta.getDefaultItem();
        if (item == null) {
            return null;
        }

        PagedIconUpdater pagedIconUpdater = menu.getPagedIconsUpdater();
        Formater formater = new Formater(pagedIconUpdater.getPage(), pagedIconUpdater.getLastPage());
        return formater.formatItem(item);
    }

    @Override
    public void clickAction(InventoryClickEvent event) {
        PagedIconUpdater pageUpdater = menu.getPagedIconsUpdater();
        nextPage = pageUpdater.getPage() + pageDirection;

        if (nextPage < 1 || nextPage > pageUpdater.getLastPage()) {
            nextPage = 0;
            return;
        }

        pageUpdater.setPage(nextPage);
        pageUpdater.update();
        menu.getIconsUpdater().update();
    }

    public NextPageIcon(IconMetadata meta, PagedMenu<?> menu, int pageDirection) {
        if (pageDirection == 0) throw new IllegalArgumentException("pageDirection cannot be 0");

        this.meta = meta;
        this.menu = menu;
        this.pageDirection = pageDirection;

        menu.getIconsUpdater().addSlot(meta.getSlots());
    }

}
