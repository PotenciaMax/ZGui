package me.zmaster.zgui.element;

import me.zmaster.zgui.Slot;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface Element {

    void render(Slot slot);

    /**
     * Called when the icon is clicked in the inventory.
     * Default implementation does nothing, so implementing classes
     * can override this method to define custom click behavior.
     *
     * @param event the InventoryClickEvent triggered by the click
     */
    default void onClick(InventoryClickEvent event) {}

}
