package me.zmaster.zgui.meta;

import me.zmaster.zgui.meta.data.ItemData;
import me.zmaster.zgui.meta.path.KeyPath;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public final class MenuMeta {

    private final String inventoryName;
    private final SlotPattern slotPattern;
    private final Map<String, List<Integer>> slots;

    private final Map<String, ElementMeta> elementMetas = new HashMap<>();

    private MenuMeta(Builder builder) {
        this.inventoryName = builder.inventoryName;
        this.slotPattern = builder.slotPattern;
        this.slots = builder.slots;
    }

    @NotNull
    public String getInventoryName() {
        return inventoryName;
    }

    public Inventory createInventory(String name) {
        return slotPattern.createInventory(name);
    }

    public Inventory createInventory() {
        return slotPattern.createInventory(inventoryName);
    }

    public Map<String, List<Integer>> getSlots() {
        return slots;
    }

    public List<Integer> getSlots(String key) {
        return slots.getOrDefault(key, Collections.emptyList());
    }

    public Map<String, ElementMeta> getElementMetas() {
        return elementMetas;
    }

    @Nullable
    public ElementMeta getElementMeta(String key) {
        return elementMetas.get(key);
    }

    public static class Builder {

        private final Map<String, Function<KeyPath, Object>> mappedData = new HashMap<>();
        private final FileConfiguration config;

        private final String inventoryName;
        private final SlotPattern slotPattern;
        private final Map<String, List<Integer>> slots = new HashMap<>();

        public Builder(FileConfiguration config) {
            this.config = config;
            this.inventoryName = buildInventoryName();
            this.slotPattern = new SlotPattern(config.getStringList("slot_pattern"));

            loadSlots();
            mapData("items", ItemData::new);
        }

        public Builder mapData(String path, Function<KeyPath, Object> factory) {
            mappedData.put(path, factory);
            return this;
        }

        public MenuMeta build() {
            MenuMeta menuMeta = new MenuMeta(this);
            slots.forEach((key, indexes) ->
                    menuMeta.elementMetas.put(key, new ElementMeta(indexes, key, config, mappedData)));

            return menuMeta;
        }

        private String buildInventoryName() {
            String name = config.getString("name");
            if (name == null) return null;
            return ChatColor.translateAlternateColorCodes('&', name);
        }

        private void loadSlots() {
            ConfigurationSection section = config.getConfigurationSection("slots");
            if (section == null) return;

            for (String key : section.getKeys(false)) {
                String charPattern = section.getString(key);
                if (charPattern == null) continue;
                List<Integer> mapped = slotPattern.getSlotsByChar(charPattern);
                slots.put(key, mapped);
            }
        }
    }

}
