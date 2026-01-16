package me.zmaster.zgui.meta.path;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KeyPath {

    private final ConfigurationSection config;
    private final String path;

    public KeyPath(ConfigurationSection config, String path) {
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

    public int asInt() {
        return config.getInt(path);
    }

    public @NotNull List<String> asStringList() {
        return config.getStringList(path);
    }

    public @NotNull List<Integer> asIntList() {
        return config.getIntegerList(path);
    }
}
