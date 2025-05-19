package me.zmaster.zgui;

import me.zmaster.zgui.menu.AbstractMenu;
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

public class ZGui {

    private static ZGui instance;
    private final Plugin plugin;
    private final Map<Inventory, AbstractMenu> registeredInventories = new HashMap<>();

    public static ZGui get() {
        if (instance == null) {
            throw new IllegalStateException("The ZGui instance is not initialized");
        }
        return instance;
    }

    public static void initialize(Plugin plugin) {
        instance = new ZGui(plugin);
    }

    public Collection<AbstractMenu> getRegisteredMenus() {
        return registeredInventories.values();
    }

    public  AbstractMenu getRegisteredMenu(Inventory inventory) {
        return registeredInventories.get(inventory);
    }

    public void registerMenu(Inventory inv, AbstractMenu menu) {
        if (inv.getViewers().isEmpty()) {
            registeredInventories.put(inv, menu);
        }
    }

    public void unregisterMenu(AbstractMenu gui) {
        Inventory inventory = gui.getInventory();
        if (inventory.getViewers().size() <= 1) {
            registeredInventories.remove(inventory);
        }
    }

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
        this.plugin = plugin;
    }

    public class GuiListener implements Listener {

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

            unregisterMenu(menu);

            // The onClose will be executed after the inventory is already closed to avoid bugs
            Bukkit.getScheduler().runTask(plugin, () -> menu.onClose(event));
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
                instance = null;
            }
        }
    }

}
