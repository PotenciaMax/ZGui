package me.zmaster.zgui.inventory.meta;

import org.bukkit.plugin.Plugin;

public class Meta {

    private final Plugin plugin;

    public Meta(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
