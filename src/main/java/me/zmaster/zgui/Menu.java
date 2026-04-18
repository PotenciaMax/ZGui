package me.zmaster.zgui;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

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
    void open(Player player);

}
