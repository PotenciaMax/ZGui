package me.zmaster.zgui.icon;

import me.zmaster.zgui.AbstractMenu;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Implementation of AbstractIconUpdater that updates static icons in a menu.
 * It iterates over the assigned slots and refreshes the items in the inventory
 * based on the current icon data stored in the menu.
 */
public class IconUpdater extends AbstractIconUpdater {

    private final AbstractMenu menu;

    @NotNull
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

    public IconUpdater(@NotNull AbstractMenu menu) {
        this.menu = Objects.requireNonNull(menu, "menu cannot be null");
    }
}
