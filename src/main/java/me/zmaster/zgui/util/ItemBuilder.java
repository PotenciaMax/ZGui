package me.zmaster.zgui.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public static ItemBuilder of(@Nullable ItemStack item) {
        return new ItemBuilder(item);
    }

    private ItemBuilder(@Nullable ItemStack item) {
        this.item = item;
        this.meta = item == null ? null : item.getItemMeta();
    }

    public ItemBuilder addLore(@NotNull List<String> lore) {
        Objects.requireNonNull(lore, "lore must not be null");
        if (meta == null) return this;

        List<String> metaLore = meta.getLore();
        if (metaLore != null) {
            metaLore.addAll(lore);
        } else {
            metaLore = lore;
        }

        meta.setLore(metaLore);
        return this;
    }

    public @Nullable ItemStack build() {
        if (item == null) return null;

        item.setItemMeta(meta);
        return item;
    }
}
