package me.zmaster.zgui.inventory;

import org.bukkit.Bukkit;
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
import java.util.UUID;

public class InventoryMenuManager {

    private final Plugin plugin;
    private final Map<UUID, AbstractMenu> registeredInventories = new HashMap<>();

    public InventoryMenuManager(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(new MenuListener(), plugin);
    }

    public Collection<AbstractMenu> getRegisteredMenus() {
        return registeredInventories.values();
    }

    public AbstractMenu getRegisteredMenu(UUID id) {
        return registeredInventories.get(id);
    }

    public void registerMenu(UUID id, AbstractMenu menu) {
        registeredInventories.put(id, menu);
    }

    public void unregisterMenu(UUID id) {
        registeredInventories.remove(id);
    }

    public void unregisterMenus(Plugin plugin) {
    }

    public class MenuListener implements Listener {

        @EventHandler
        public void openListener(InventoryOpenEvent event) {
            AbstractMenu menu = getRegisteredMenu(event.getPlayer().getUniqueId());

            if (menu != null) menu.onOpen(event);
        }

        @EventHandler
        public void closeListener(InventoryCloseEvent event) {
            AbstractMenu menu = getRegisteredMenu(event.getPlayer().getUniqueId());

            if (menu == null) return;

            // The onClose will be executed after the inventory is already closed to avoid bugs
            Bukkit.getScheduler().runTask(plugin, () -> {
                menu.onClose(event);
                unregisterMenu(event.getPlayer().getUniqueId());
            });
        }


        @EventHandler
        public void clickListener(InventoryClickEvent event) {
            AbstractMenu menu = getRegisteredMenu(event.getWhoClicked().getUniqueId());

            if (menu == null) return;

            event.setCancelled(true);

            Inventory clickedInv = event.getClickedInventory();
            if (clickedInv == null) return;

            if (clickedInv.equals(event.getView().getTopInventory())) {
                menu.onClick(event);
            } else if (clickedInv.equals(event.getView().getBottomInventory())) {
                menu.onBottomClick(event);
            }
        }

        @EventHandler
        public void dragListener(InventoryDragEvent event) {
            AbstractMenu menu = getRegisteredMenu(event.getWhoClicked().getUniqueId());

            if (menu != null) event.setCancelled(true);
        }

        @EventHandler
        public void onPluginDisable(PluginDisableEvent event) {
            unregisterMenus(event.getPlugin());
        }
    }
}
