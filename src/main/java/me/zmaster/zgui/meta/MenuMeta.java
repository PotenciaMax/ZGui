package me.zmaster.zgui.meta;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class MenuMeta<E extends ElementMeta> {

    public static MenuMeta<ElementMeta> from(ConfigurationSection config) {
        return new MenuMeta<ElementMeta>(config) {
            @Override
            protected ElementMeta buildElementMeta(ConfigurationSection section) {
                return new ElementMeta(this, section);
            }
        };
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

    public @NotNull Map<String, ElementMeta> getElementMetas() {
        return Collections.unmodifiableMap(elementMetas);
    }

    public @Nullable ElementMeta getElementMeta(String key) {
        return elementMetas.get(key);
    }

    public @NotNull Inventory createInventory() {
        return slotPattern.createInventory(inventoryName);
    }

    public Inventory createInventory(String name) {
        return slotPattern.createInventory(name);
    }

    protected abstract E buildElementMeta(ConfigurationSection section);

}
