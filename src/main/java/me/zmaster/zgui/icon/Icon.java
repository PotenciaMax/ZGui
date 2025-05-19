package me.zmaster.zgui.icon;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface Icon {
    ItemStack getItem();

    default void clickAction(InventoryClickEvent event) {

    }

}
