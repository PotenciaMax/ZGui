package me.zmaster.zgui.icon;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class IconMetadata {

    public static final String DEFAULT_STATE = "default";

    private final char slot;
    private final Map<String, ItemStack> itemStates = new HashMap<>();

    public char getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return itemStates.get(DEFAULT_STATE);
    }

    public ItemStack getItem(String state) {
        return itemStates.get(state);
    }

    public ItemStack getItemOrDefault(String state) {
        return itemStates.getOrDefault(state, getItem());
    }

    public IconMetadata(YamlConfiguration file, String key) {
        this.slot = file.getObject("slots." + key, char.class);

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
            itemStates.put(subKey.toString(), getItem(subSection));
        }
    }

    private void setupDefaultItem(ConfigurationSection itemSection) {
        itemStates.put(DEFAULT_STATE, getItem(itemSection));
    }

    private ItemStack getItem(ConfigurationSection section) {
        String materialName = section.getString("type", "STONE");
        XMaterial material = XMaterial.matchXMaterial(materialName).orElse(XMaterial.STONE);

        ItemStack item = material.parseItem();
        if (item == null) {
            item = new ItemStack(Material.STONE);
        }

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

        String base64 = section.getString("base64");
        if (base64 != null && material == XMaterial.PLAYER_HEAD) {
            SkullUtils.applySkin(meta, base64);
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
