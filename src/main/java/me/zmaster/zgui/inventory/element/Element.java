package me.zmaster.zgui.inventory.element;

import me.zmaster.zgui.inventory.context.ClickContext;
import me.zmaster.zgui.inventory.meta.ElementMeta;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Element extends Icon {

    static @NotNull Element from(List<Integer> slots, ItemStack item, Clickable click) {
        return new Element() {
            @Override
            public @NotNull List<Integer> getSlots() {
                return slots;
            }

            @Override
            public ItemStack getItem() {
                return item;
            }

            @Override
            public void onClick(ClickContext context) {
                click.onClick(context);
            }
        };
    }

    static @NotNull Element from(ElementMeta meta, String state, Clickable click) {
        return from(meta.getSlots(), meta.getItem(state), click);
    }

    static @NotNull Element from(ElementMeta meta, Clickable click) {
        return from(meta.getSlots(), meta.getDefaultItem(), click);
    }

    static @NotNull Element from(List<Integer> slots, ItemStack item) {
        return new Element() {
            @Override
            public @NotNull List<Integer> getSlots() {
                return slots;
            }

            @Override
            public ItemStack getItem() {
                return item;
            }
        };
    }

    static @NotNull Element from(ElementMeta meta, String state) {
        return from(meta.getSlots(), meta.getItem(state));
    }

    static @NotNull Element from(ElementMeta meta) {
        return from(meta.getSlots(), meta.getDefaultItem());
    }

    @NotNull List<Integer> getSlots();
}
