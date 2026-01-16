package me.zmaster.zgui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class responsible for managing and registering menus (GUIs) in the plugin.
 * <p>
 * It keeps track of registered menus linked to their inventories,
 * handles menu registration, unregistration, and event listener registration.
 * Ensures only one instance exists during the plugin lifecycle.
 */
public class ZGui {

    private static final Plugin plugin = JavaPlugin.getProvidingPlugin(ZGui.class);
    private static final ZGui instance = new ZGui(plugin);

    private final Map<Inventory, AbstractMenu> registeredInventories = new HashMap<>();

    /**
     * Returns the singleton instance of ZGui.
     * Throws IllegalStateException if called before initialization.
     *
     * @return the ZGui instance
     */
    public static ZGui get() {
        if (instance == null) {
            throw new IllegalStateException("The ZGui instance is not initialized");
        }
        return instance;
    }

    /**
     * Returns a collection of all currently registered menus.
     *
     * @return a collection of AbstractMenu objects
     */
    public Collection<AbstractMenu> getRegisteredMenus() {
        return registeredInventories.values();
    }

    /**
     * Returns the menu registered for the specified inventory, or null if none.
     *
     * @param inventory the inventory to look up
     * @return the registered AbstractMenu or null if not found
     */
    public AbstractMenu getRegisteredMenu(Inventory inventory) {
        return registeredInventories.get(inventory);
    }

    /**
     * Registers a menu associated with an inventory.
     *
     * @param menu the menu associated with the inventory
     */
    public void registerMenu(AbstractMenu menu) {
        registeredInventories.put(menu.inventory, menu);
    }

    /**
     * Unregisters the given menu if the associated inventory has at most one viewer.
     *
     * @param menu the AbstractMenu to unregister
     */
    public void unregisterMenu(AbstractMenu menu) {
        registeredInventories.remove(menu.inventory);
    }

    /**
     * Unregisters all menus and closes inventories for all online players who have a registered menu open.
     */
    public void unregisterMenus() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Inventory openInv = player.getOpenInventory().getTopInventory();
            if (registeredInventories.containsKey(openInv)) {
                player.closeInventory();
            }
        }
        registeredInventories.clear();
    }

    private ZGui(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new MenuListener(), plugin);
    }

    public class MenuListener implements Listener {

        @EventHandler
        public void openListener(InventoryOpenEvent event) {
            AbstractMenu menu = getRegisteredMenu(event.getInventory());

            if (menu == null) {
                return;
            }

            menu.onOpen(event);
        }

        @EventHandler
        public void closeListener(InventoryCloseEvent event) {
            AbstractMenu menu = getRegisteredMenu(event.getInventory());

            if (menu == null) {
                return;
            }

            // The onClose will be executed after the inventory is already closed to avoid bugs
            Bukkit.getScheduler().runTask(plugin, () -> {
                menu.onClose(event);
                if (menu.inventory.getViewers().isEmpty()) unregisterMenu(menu);
            });
        }


        @EventHandler
        public void clickListener(InventoryClickEvent event) {
            AbstractMenu menu = getRegisteredMenu(event.getInventory());

            if (menu == null) {
                return;
            }

            event.setCancelled(true);

            if (event.getClickedInventory() == null) {
                return;
            }

            menu.onClick(event);
        }

        @EventHandler
        public void dragListener(InventoryDragEvent event) {
            AbstractMenu menu = getRegisteredMenu(event.getInventory());

            if (menu == null) {
                return;
            }

            event.setCancelled(true);
        }

        @EventHandler
        public void onPluginDisable(PluginDisableEvent event) {
            if (plugin.equals(event.getPlugin())) {
                unregisterMenus();
            }
        }
    }
}
