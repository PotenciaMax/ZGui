package me.zmaster.zgui.menu;

import org.bukkit.entity.HumanEntity;

/**
 * Represents a generic menu in the GUI system.
 * Provides methods to open this menu / previous for a player.
 */
public interface Menu {
    /**
     * Opens this menu for the specified player.
     *
     * @param player the player to open the menu for
     */
    void open(HumanEntity player);

    /**
     * Opens the previous menu for the player if it exists;
     * otherwise, closes the inventory.
     *
     * @param player the player to open the previous menu for
     */
    void openPrevious(HumanEntity player);
}
