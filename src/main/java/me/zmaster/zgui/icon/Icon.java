package me.zmaster.zgui.icon;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Functional interface representing an icon in a menu.
 * Provides the item to be displayed and optionally handles click actions.
 */
@FunctionalInterface
public interface Icon {

    /**
     * Returns the ItemStack that represents this icon in the inventory.
     *
     * @return the item to be displayed
     */
    ItemStack getItem();

    /**
     * Called when the icon is clicked in the inventory.
     * Default implementation does nothing, so implementing classes
     * can override this method to define custom click behavior.
     *
     * @param event the InventoryClickEvent triggered by the click
     */
    default void clickAction(InventoryClickEvent event) {}
}
