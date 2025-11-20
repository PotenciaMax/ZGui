package me.zmaster.zgui.meta.path;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

public class KeyPath {

    private final FileConfiguration config;
    private final String path;

    public KeyPath(FileConfiguration config, String path) {
        this.config = config;
        this.path = path;
    }

    public boolean exists() {
        return config.contains(path);
    }

    public @Nullable ConfigurationSection asSection() {
        return config.getConfigurationSection(path);
    }

    public @Nullable String asString() {
        return config.getString(path);
    }
}
