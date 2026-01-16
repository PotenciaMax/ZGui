package me.zmaster.zgui.meta;

import me.zmaster.zgui.meta.data.ItemData;
import me.zmaster.zgui.meta.data.SlotsData;
import me.zmaster.zgui.meta.path.KeyPath;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public final class ElementMeta extends AbstractMeta {

    ElementMeta(ConfigurationSection config, Map<String, Function<KeyPath, Object>> mapping) {
        super(config, mapping);
    }

    /**
     * Returns the list of inventory slot indices for this element
     *
     * @return list of slot indices
     */
    public List<Integer> getSlots() {
        return getData("slot", SlotsData.class).map(SlotsData::getSlots).orElse(Collections.emptyList());
    }

    public @NotNull Optional<ItemData> getItemData() {
        return getData("item", ItemData.class);
    }

    /**
     * Returns the default ItemStack for this element, or null if not set.
     *
     * @return default ItemStack or null
     */
    @Nullable
    public ItemStack getDefaultItem() {
        return getItemData().map(ItemData::getDefaultItem).orElse(null);
    }

    /**
     * Returns the ItemStack of this element for the specified state if present,
     *
     * @param state the state name
     * @return the ItemStack for the state or null if absent
     */
    @Nullable
    public ItemStack getItem(String state) {
        return getItemData().map(data -> data.getItem(state)).orElse(null);
    }

    @Nullable
    public ItemStack getItemOrDefault(String state) {
        return Optional.ofNullable(getItem(state)).orElse(getDefaultItem());
    }

    @Nullable
    public ItemStack getItemOrDefault(String state, String def) {
        return Optional.ofNullable(getItem(state)).orElse(getItem(def));
    }

}
