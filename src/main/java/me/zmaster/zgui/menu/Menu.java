package me.zmaster.zgui.menu;

import org.bukkit.entity.HumanEntity;

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
