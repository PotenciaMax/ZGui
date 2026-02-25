package me.zmaster.zgui.meta;

import me.zmaster.zgui.meta.data.ItemData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ElementMeta {

    private final List<Integer> slots;
    private final ItemData itemData;
    private final boolean autoApply;

    public ElementMeta(MenuMeta<?> menuMeta, ConfigurationSection config) {
        this.slots = menuMeta.getSlotPattern().getSlotsByChar(config.getString("slot"));
        ConfigurationSection itemDataSec = config.getConfigurationSection("item");
        this.itemData = itemDataSec != null ? new ItemData(itemDataSec) : null;
        this.autoApply = config.getBoolean("static");
    }

    /**
     * Returns the list of inventory slot indices for this element
     *
     * @return list of slot indices
     */
    public List<Integer> getSlots() {
        return Collections.unmodifiableList(slots);
    }

    public @NotNull Optional<ItemData> getItemData() {
        return Optional.ofNullable(itemData);
    }

    public boolean isAutoApply() {
        return autoApply;
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
