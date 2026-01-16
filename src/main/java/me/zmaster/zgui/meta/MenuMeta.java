package me.zmaster.zgui.meta;

import me.zmaster.zgui.meta.data.ItemData;
import me.zmaster.zgui.meta.data.SlotsData;
import me.zmaster.zgui.meta.path.KeyPath;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public final class MenuMeta extends AbstractMeta {

    private final String inventoryName;
    private final SlotPattern slotPattern;
    private final Map<String, ElementMeta> elementMetas = new HashMap<>();

    MenuMeta(Builder builder) {
        super(builder.config, builder.mappedData);
        this.inventoryName = builder.inventoryName;
        this.slotPattern = builder.slotPattern;

        ConfigurationSection elements = builder.config.getConfigurationSection("elements");
        if (elements != null) {
            for (String key : elements.getKeys(false)) {
                ConfigurationSection elementSec = elements.getConfigurationSection(key);
                if (elementSec == null) continue;
                elementMetas.put(key, new ElementMeta(elementSec, builder.mappedElementsData));
            }
        }
    }

    @NotNull
    public String getInventoryName() {
        return inventoryName;
    }

    public Map<String, ElementMeta> getElementMetas() {
        return Collections.unmodifiableMap(elementMetas);
    }

    @Nullable
    public ElementMeta getElementMeta(String key) {
        return elementMetas.get(key);
    }

    public Inventory createInventory() {
        return slotPattern.createInventory(inventoryName);
    }

    public Inventory createInventory(String name) {
        return slotPattern.createInventory(name);
    }

    public List<Integer> getPagedSlots() {
        return getData("paged_slots", SlotsData.class).map(SlotsData::getSlots).orElse(Collections.emptyList());
    }

    public static class Builder {

        private final FileConfiguration config;
        private final String inventoryName;
        private final SlotPattern slotPattern;
        private final Map<String, Function<KeyPath, Object>> mappedData = new HashMap<>();
        private final Map<String, Function<KeyPath, Object>> mappedElementsData = new HashMap<>();

        public Builder(@NotNull FileConfiguration config) {
            this.config = Objects.requireNonNull(config, "config might not be null");
            this.inventoryName = Optional.ofNullable(config.getString("name"))
                    .map(name -> ChatColor.translateAlternateColorCodes('&', name))
                    .orElse("");
            this.slotPattern = new SlotPattern(config.getStringList("slot_pattern"));

            mapData("paged_slots", path -> new SlotsData(path, slotPattern));
            mapElementData("slot", path -> new SlotsData(path, slotPattern));
            mapElementData("item", ItemData::new);
        }

        public FileConfiguration getConfig() {
            return config;
        }

        public String getInventoryName() {
            return inventoryName;
        }

        public SlotPattern getSlotPattern() {
            return slotPattern;
        }

        public Builder mapData(String path, Function<KeyPath, Object> factory) {
            mappedData.put(path, factory);
            return this;
        }

        public Builder mapElementData(String path, Function<KeyPath, Object> factory) {
            mappedElementsData.put(path, factory);
            return this;
        }

        public MenuMeta build() {
            return new MenuMeta(this);
        }

    }

}
