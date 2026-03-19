package me.zmaster.zgui.inventory.meta.data;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemData {

    public static final String DEFAULT_STATE = "default";
    private final Map<String, ItemStack> items = new HashMap<>();

    public ItemData(@NotNull ConfigurationSection section) {
        Objects.requireNonNull(section, "section might not be null");

        if (isNested(section)) {
            setupNestedItems(section);
        } else {
            setupDefaultItem(section);
        }
    }

    /**
     * Returns a map of all item states associated with this data.
     * Each key corresponds to a state name and maps to an ItemStack.
     *
     * @return map of state names to ItemStacks
     */
    @NotNull
    public Map<String, ItemStack> getItems() {
        return items;
    }

    /**
     * Returns the default ItemStack for this icon, or null if not set.
     *
     * @return default ItemStack or null
     */
    @Nullable
    public ItemStack getDefaultItem() {
        return getItem(DEFAULT_STATE);
    }

    /**
     * Returns the ItemStack for the specified state if present,
     *
     * @param state the state name
     * @return the ItemStack for the state or null if absent
     */
    @Nullable
    public ItemStack getItem(String state) {
        ItemStack item = items.get(state);
        return item != null ? item.clone() : null;
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

    private void setupNestedItems(@NotNull ConfigurationSection section) {
        for (Object subKey : section.getKeys(false)) {
            ConfigurationSection subSection = section.getConfigurationSection(subKey.toString());
            items.put(subKey.toString(), buildItem(Objects.requireNonNull(subSection)));
        }
    }

    private void setupDefaultItem(ConfigurationSection itemSection) {
        items.put(DEFAULT_STATE, buildItem(itemSection));
    }

    private ItemStack buildItem(ConfigurationSection section) {
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
            lore.forEach((text) -> {
                coloredLore.add(ChatColor.translateAlternateColorCodes('&', text));
            });
            meta.setLore(coloredLore);
        }

        //String base64 = section.getString("base64");
        //if (base64 != null && material == XMaterial.PLAYER_HEAD) {
        //    Profileable profileable = Profileable.of(ProfileInputType.BASE64, base64);
        //    meta = XSkull.of(meta).profile(profileable).apply();
        //}

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
