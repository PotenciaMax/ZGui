package me.zmaster.zgui.util;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Utility class for formatting strings and item metadata with placeholders.
 */
public class Formater {

    private Object[] placeholders;

    /**
     * Creates a new Formater with the given placeholders.
     *
     * @param placeholders initial placeholders to use
     */
    public Formater(Object... placeholders) {
        this.placeholders = placeholders;
    }

    /**
     * Adds placeholders to be used when formatting strings.
     *
     * @param placeholders objects to replace placeholders in strings
     */
    public void addPlaceholders(Object... placeholders) {
        this.placeholders = ArrayUtils.addAll(this.placeholders, placeholders);
    }

    /**
     * Formats a string replacing placeholders with the stored values.
     *
     * @param str the string with placeholders
     * @return formatted string
     */
    public String formatString(String str) {
        return MessageFormat.format(str, placeholders);
    }

    /**
     * Formats a list of strings replacing placeholders.
     *
     * @param list list of strings to format
     * @return a new list with formatted strings
     */
    public List<String> formatStringList(@NotNull List<String> list) {
        Objects.requireNonNull(list, "lore cannot be null");

        List<String> formatedLore = new ArrayList<>(list.size());
        for (String line : list) {
            formatedLore.add(formatString(line));
        }

        return formatedLore;
    }

    /**
     * Formats an ItemStack's display name and lore replacing placeholders.
     *
     * @param item the ItemStack to format
     * @return a new formatted ItemStack clone
     */
    public ItemStack formatItem(@NotNull ItemStack item) {
        ItemStack formatedItem = Objects.requireNonNull(item, "item cannot be null").clone();

        ItemMeta meta = formatedItem.getItemMeta();
        String displayName = meta.getDisplayName();
        List<String> lore = meta.getLore();

        if (meta.hasDisplayName()) {
            meta.setDisplayName(formatString(displayName));
        }

        if (lore != null) {
            meta.setLore(formatStringList(lore));
        }

        formatedItem.setItemMeta(meta);
        return formatedItem;
    }
}
