package me.zmaster.zgui.meta;

import me.zmaster.zgui.meta.path.KeyPath;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractMeta {

    private final Map<String, Object> data = new HashMap<>();

    AbstractMeta(ConfigurationSection config, Map<String, Function<KeyPath, Object>> mapping) {
        mapping.forEach((key, factory) -> {
            KeyPath path = new KeyPath(config, key);
            if (path.exists()) {
                data.put(key, factory.apply(path));
            }
        });
    }

    public boolean containsData(String key) {
        return data.containsKey(key);
    }

    public <T> @NotNull Optional<T> getData(String key, Class<T> type) {
        return Optional.ofNullable(type.cast(data.get(key)));
    }

    public @NotNull Optional<Object> getData(String key) {
        return Optional.ofNullable(data.get(key));
    }

}
