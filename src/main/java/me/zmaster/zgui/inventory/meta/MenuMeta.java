package me.zmaster.zgui.inventory.meta;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;

public abstract class MenuMeta<E extends ElementMeta> {

    public static <E extends ElementMeta> MenuMeta<E> withElementMeta(ConfigurationSection config, BiFunction<MenuMeta<E>, ConfigurationSection, E> factory) {
        return new MenuMeta<E>(config) {
            @Override
            protected E buildElementMeta(ConfigurationSection section) {
                return factory.apply(this, section);
            }
        };
    }

    public static MenuMeta<ElementMeta> create(ConfigurationSection config) {
        return withElementMeta(config, ElementMeta::new);
    }

    private final String inventoryName;
    private final SlotPattern slotPattern;
    private final List<Integer> pagedSlots;
    private final Map<String, E> elementMetas = new HashMap<>();

    public MenuMeta(ConfigurationSection config) {
        this.inventoryName = ChatColor.translateAlternateColorCodes('&', config.getString("name", ""));
        this.slotPattern = new SlotPattern(config.getStringList("slot_pattern"));
        this.pagedSlots = slotPattern.getSlotsByChar(config.getString("paged_slots"));

        ConfigurationSection elementsSec = config.getConfigurationSection("elements");
        if (elementsSec != null) {
            for (String key : elementsSec.getKeys(false)) {
                ConfigurationSection elementSec = elementsSec.getConfigurationSection(key);
                if (elementSec == null) continue;
                elementMetas.put(key, buildElementMeta(elementSec));
            }
        }
    }

    public @NotNull String getInventoryName() {
        return inventoryName;
    }

    public @NotNull SlotPattern getSlotPattern() {
        return slotPattern;
    }

    public @NotNull List<Integer> getPagedSlots() {
        return pagedSlots;
    }

    public @NotNull Map<String, E> getElementMetas() {
        return Collections.unmodifiableMap(elementMetas);
    }

    public @Nullable E getElementMeta(String key) {
        return elementMetas.get(key);
    }

    public @NotNull Inventory createInventory() {
        return slotPattern.createInventory(inventoryName);
    }

    public @NotNull Inventory createInventory(String name) {
        return slotPattern.createInventory(name);
    }

    protected abstract E buildElementMeta(ConfigurationSection section);

}
