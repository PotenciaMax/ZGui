package me.zmaster.zgui.menu;

import org.bukkit.entity.HumanEntity;

public interface Menu {
    /**
     * Open the menu for the player.
     *
     * @param player ...
     */
    void open(HumanEntity player);

    /**
     * Open the previous menu for the player.
     * If theirs not a previous menu closes the
     * menu.
     *
     * @param player ...
     */
    void openPrevious(HumanEntity player);
}
