package me.zmaster.zgui.util;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Set;

public class MetaMerger {

    private final ItemStack item;

    public void mergeMeta(ItemStack base) {
        ItemMeta meta = item.getItemMeta();
        ItemMeta baseMeta = base.getItemMeta();

        if (baseMeta.hasDisplayName()) {
            meta.setDisplayName(baseMeta.getDisplayName());
        }

        List<String> baseLore = baseMeta.getLore();
        if (baseLore != null) {
            List<String> lore = meta.getLore();
            if (lore == null) {
                meta.setLore(baseLore);
            } else {
                lore.addAll(baseLore);
                meta.setLore(lore);
            }
        }

        if (baseMeta.hasEnchants()) {
            baseMeta.getEnchants().forEach((e, l) -> meta.addEnchant(e, l, true));
        }

        Set<ItemFlag> baseFlags = baseMeta.getItemFlags();
        if (!baseFlags.isEmpty()) {
            baseFlags.forEach(meta::addItemFlags);
        }

        item.setItemMeta(meta);
    }

    public MetaMerger(ItemStack item) {
        this.item = item;
    }
}
