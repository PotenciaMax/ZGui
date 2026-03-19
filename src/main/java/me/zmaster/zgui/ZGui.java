package me.zmaster.zgui;

import me.zmaster.zgui.inventory.InventoryMenuManager;
import me.zmaster.zgui.sign.SignMenuManager;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class ZGui {

    private static final Plugin plugin = JavaPlugin.getProvidingPlugin(ZGui.class);
    private static final ZGui instance = new ZGui(plugin);

    private final InventoryMenuManager inventoryMenuManager;
    private final SignMenuManager signMenuManager;

    public static ZGui get() {
        if (instance == null) {
            throw new IllegalStateException("The ZGui instance is not initialized");
        }
        return instance;
    }

    public InventoryMenuManager getInventoryMenuManager() {
        return inventoryMenuManager;
    }

    public SignMenuManager getSignMenuManager() {
        return signMenuManager;
    }

    private ZGui(Plugin plugin) {
        this.inventoryMenuManager = new InventoryMenuManager(plugin);
        this.signMenuManager = new SignMenuManager();
    }

}
