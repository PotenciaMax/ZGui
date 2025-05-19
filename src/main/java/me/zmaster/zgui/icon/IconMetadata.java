package me.zmaster.zgui.icon;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

    public static final String DEFAULT_STATE = "default";

    private final char slot;
    private final Map<String, ItemStack> itemStates = new HashMap<>();
    private final Map<String, XSound> soundStates = new HashMap<>();

    public char getSlot() {
        return slot;
    }

    public @Nullable ItemStack getItem() {
        return itemStates.get(DEFAULT_STATE);
    }

    public @Nullable ItemStack getItem(String state) {
        return itemStates.get(state);
    }

    public @Nullable ItemStack getItemOrDefault(String state) {
        return itemStates.getOrDefault(state, getItem());
    }


    public IconMetadata(YamlDocument file, String key) {
        this.slot = file.getChar("slots." + key);

        Section itemSection = file.getSection("items." + key);
        if (itemSection != null && !itemSection.isEmpty(true)) {
            // Configura itens com base na estrutura da seção
            if (isNested(itemSection)) {
                setupNestedItems(itemSection);
            } else {
                setupDefaultItem(itemSection);
            }
        }
    }

    private boolean isNested(Section itemSection) {
        for (Object key : itemSection.getKeys()) {
            Section subSection = itemSection.getSection(key.toString());
            if (subSection != null) {
                return true; // Se encontrar uma sub-seção, retorna true
            }
        }
        return false; // Nenhuma sub-seção encontrada
    }

    private void setupNestedItems(Section itemSection) {
        for (Object subKey : itemSection.getKeys()) {
            Section subSection = itemSection.getSection(subKey.toString());
            itemStates.put(subKey.toString(), getItem(subSection));
        }
    }

    private void setupDefaultItem(Section itemSection) {
        itemStates.put(DEFAULT_STATE, getItem(itemSection));
    }

    private ItemStack getItem(@NotNull Section section) {
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
        if (lore != null) {
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
