package me.zmaster.zgui.icon;

import me.zmaster.zgui.menu.SlotPattern;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class IconMetadata {

    /**
     * The default state key for icons without specific states.
     */
    public static final String DEFAULT_STATE = "default";

    private final List<Integer> slots;
    private final Map<String, ItemStack> itemStates = new HashMap<>();

    /**
     * Returns the list of inventory slot indices where this icon should be placed.
     *
     * @return list of slot indices
     */
    public List<Integer> getSlots() {
        return slots;
    }

    /**
     * Returns a map of all item states associated with this icon.
     * Each key corresponds to a state name and maps to an ItemStack.
     *
     * @return map of state names to ItemStacks
     */
    @NotNull
    public Map<String, ItemStack> getItems() {
        return itemStates;
    }

    /**
     * Returns the default ItemStack for this icon, or null if not set.
     *
     * @return default ItemStack or null
     */
    @Nullable
    public ItemStack getDefaultItem() {
        return itemStates.get(DEFAULT_STATE);
    }

    /**
     * Returns the ItemStack for the specified state if present,
     * otherwise returns the default ItemStack.
     *
     * @param state the state name
     * @return the ItemStack for the state or default if absent
     */
    @Nullable
    public ItemStack getItemOrDefault(String state) {
        return itemStates.getOrDefault(state, getDefaultItem());
    }

    public IconMetadata(FileConfiguration file, String key, SlotPattern slotPattern) {
        String slot = file.getString("slots." + key);
        this.slots = slotPattern.getSlotsByChar(slot);

        ConfigurationSection itemSection = file.getConfigurationSection("items." + key);
        if (itemSection != null) {
            if (isNested(itemSection)) {
                setupNestedItems(itemSection);
            } else {
                setupDefaultItem(itemSection);
            }
        }
    }

    private boolean isNested(ConfigurationSection itemSection) {
        for (Object key : itemSection.getKeys(false)) {
            ConfigurationSection subSection = itemSection.getConfigurationSection(key.toString());
            if (subSection != null) {
                return true;
            }
        }
        return false;
    }

    private void setupNestedItems(ConfigurationSection itemSection) {
        for (Object subKey : itemSection.getKeys(false)) {
            ConfigurationSection subSection = itemSection.getConfigurationSection(subKey.toString());
            itemStates.put(subKey.toString(), buildItem(subSection));
        }
    }

    private void setupDefaultItem(ConfigurationSection itemSection) {
        itemStates.put(DEFAULT_STATE, buildItem(itemSection));
    }

    private ItemStack buildItem(ConfigurationSection section) {
        String materialName = section.getString("type", "STONE");
        Material material = Material.valueOf(materialName);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        String displayName = section.getString("name");
        if (displayName != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        }

        List<String> lore = section.getStringList("lore");
        if (!lore.isEmpty()) {
            List<String> coloredLore = new ArrayList<>();
            lore.forEach(text -> coloredLore.add(ChatColor.translateAlternateColorCodes('&', text)));
            meta.setLore(coloredLore);
        }

        if (section.getBoolean("glow")) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
            item.addUnsafeEnchantment(Enchantment.LUCK, 1);
        } else {
            item.setItemMeta(meta);
        }

        return item;
    }
}
