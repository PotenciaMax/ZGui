package me.zmaster.zgui.icon;

import me.zmaster.zgui.menu.AbstractMenu;
import org.bukkit.inventory.Inventory;

public class IconUpdater extends AbstractIconUpdater {

    private final AbstractMenu menu;

    @Override
    public AbstractMenu getMenu() {
        return menu;
    }

    @Override
    public void update() {
        Inventory inv = getMenu().getInventory();

        for (int slot : getSlots()) {
            Icon icon = getMenu().getIcons().get(slot);
            inv.setItem(slot, icon != null ? icon.getItem() : null);
        }
    }

    public IconUpdater(AbstractMenu menu) {
        this.menu = menu;
    }
}
