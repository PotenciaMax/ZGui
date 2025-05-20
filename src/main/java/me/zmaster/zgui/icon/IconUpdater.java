package me.zmaster.zgui.icon;

import me.zmaster.zgui.menu.AbstractMenu;
import org.bukkit.inventory.Inventory;

import java.util.TreeSet;

public class IconUpdater {

    private final TreeSet<Integer> slots = new TreeSet<>();
    private final AbstractMenu menu;

    public IconUpdater(AbstractMenu menu, char... slot) {
        this.menu = menu;
        addSlot(slot);
    }

    public IconUpdater(AbstractMenu menu) {
        this.menu = menu;
    }

    public void addSlot(char... slot) {
        slots.addAll(menu.getSlotPattern().getSlotsByChar(slot));
    }

    public void removeSlot(char... slot) {
        slots.removeAll(menu.getSlotPattern().getSlotsByChar(slot));
    }

    public AbstractMenu getMenu() {
        return menu;
    }

    public void update() {
        Inventory inv = getMenu().getInventory();

        for (int slot : slots) {
            Icon icon = getMenu().getIconMap().get(slot);

            if (icon == null) {
                inv.setItem(slot, null);
            } else {
                inv.setItem(slot, icon.getItem());
            }
        }
    }
}
