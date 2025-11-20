package me.zmaster.zgui.meta;

import me.zmaster.zgui.meta.data.ItemData;
import me.zmaster.zgui.meta.path.KeyPath;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public final class ElementMeta {

    private final List<Integer> slots;
    private final Map<Class<?>, Object> keys = new HashMap<>();

    ElementMeta(List<Integer> slots, String key, FileConfiguration config, Map<String, Function<KeyPath, Object>> paths) {
        this.slots = slots;

        for (Map.Entry<String, Function<KeyPath, Object>> path : paths.entrySet()) {
            KeyPath keyPath = new KeyPath(config, path.getKey() + "." + key);
            if (keyPath.exists()) continue;

            Object keyMeta = path.getValue().apply(keyPath);
            keys.put(keyMeta.getClass(), keyMeta);
        }
    }

    /**
     * Returns the list of inventory slot indices for this element
     *
     * @return list of slot indices
     */
    public List<Integer> getSlots() {
        return Collections.unmodifiableList(slots);
    }

    public <T> @Nullable T getData(Class<T> clazz) {
        return clazz.cast(keys.get(clazz));
    }

    public ItemData getItemData() {
        return Objects.requireNonNull(getData(ItemData.class));
    }

    /**
     * Returns the default ItemStack for this element, or null if not set.
     *
     * @return default ItemStack or null
     */
    @Nullable
    public ItemStack getDefaultItem() {
        return getItemData().getDefaultItem();
    }

    /**
     * Returns the ItemStack of this element for the specified state if present,
     *
     * @param state the state name
     * @return the ItemStack for the state or null if absent
     */
    @Nullable
    public ItemStack getItem(String state) {
        return getItemData().getItem(state);
    }

}
