package me.zmaster.zgui.util;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Set;

/**
 * Utility class to merge metadata from a base ItemStack into a target ItemStack.
 */
public class MetaMerger {

    private final ItemStack item;

    /**
     * Merges metadata from the given base ItemStack into the target item.
     * This includes display name, lore, enchantments, and item flags.
     *
     * @param base the ItemStack to merge metadata from
     */
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

    /**
     * Creates a new MetaMerger to merge metadata into the specified item.
     *
     * @param item the target ItemStack to receive metadata
     */
    public MetaMerger(ItemStack item) {
        this.item = item;
    }
}
