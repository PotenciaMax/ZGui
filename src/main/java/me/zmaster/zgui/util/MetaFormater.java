package me.zmaster.zgui.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MetaFormater {

    private final Object[] placeholders;

    public ItemStack formatItem(@NotNull ItemStack item) {
        ItemStack formatedItem = Objects.requireNonNull(item, "item cannot be null").clone();

        ItemMeta meta = formatedItem.getItemMeta();
        String displayName = meta.getDisplayName();
        List<String> lore = meta.getLore();

        if (meta.hasDisplayName()) {
            meta.setDisplayName(MessageFormat.format(displayName, placeholders));
        }

        if (lore != null) {
            meta.setLore(formatLore(lore));
        }

        formatedItem.setItemMeta(meta);
        return formatedItem;
    }

    public List<String> formatLore(@NotNull List<String> lore) {
        Objects.requireNonNull(lore, "lore cannot be null");

        List<String> formatedLore = new ArrayList<>(lore.size());
        for (String line : lore) {
            formatedLore.add(MessageFormat.format(line, placeholders));
        }

        return formatedLore;
    }

    public MetaFormater(Object... placeholders) {
        this.placeholders = placeholders;
    }

}
