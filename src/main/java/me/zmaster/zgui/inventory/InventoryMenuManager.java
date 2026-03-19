package me.zmaster.zgui.inventory;

import me.zmaster.zgui.ZGui;
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InventoryMenuManager {

    private final Plugin plugin;
    private final Map<Inventory, AbstractMenu> registeredInventories = new HashMap<>();

    public InventoryMenuManager(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(new MenuListener(), plugin);
    }

    public Collection<AbstractMenu> getRegisteredMenus() {
        return registeredInventories.values();
    }

    public AbstractMenu getRegisteredMenu(Inventory inventory) {
        return registeredInventories.get(inventory);
    }

    public void registerMenu(AbstractMenu menu) {
        registeredInventories.put(menu.inventory, menu);
    }

    public void unregisterMenu(AbstractMenu menu) {
        registeredInventories.remove(menu.inventory);
    }

    public void unregisterMenus(Plugin plugin) {
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

            if (menu == null) return;

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
            unregisterMenus(event.getPlugin());
        }
    }
}
