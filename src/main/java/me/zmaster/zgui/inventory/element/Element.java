package me.zmaster.zgui.inventory.element;

import me.zmaster.zgui.inventory.AbstractMenu;
import me.zmaster.zgui.inventory.context.ClickContext;
import me.zmaster.zgui.inventory.meta.ElementMeta;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Element extends Icon {

    static @NotNull Element create(List<Integer> slots, ItemStack item, Clickable click) {
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
            public void onClick(@NotNull ClickContext context) {
                click.onClick(context);
            }
        };
    }

    static @NotNull Element create(List<Integer> slots, ItemStack item) {
        return create(slots, item, click -> {});
    }

    static @NotNull Element fromMeta(ElementMeta meta, String state, Clickable click) {
        return create(meta.getSlots(), meta.getItem(state), click);
    }

    static @NotNull Element fromMeta(ElementMeta meta, Clickable click) {
        return create(meta.getSlots(), meta.getDefaultItem(), click);
    }

    static @NotNull Element fromMeta(ElementMeta meta, String state) {
        return create(meta.getSlots(), meta.getItem(state));
    }

    static @NotNull Element fromMeta(ElementMeta meta) {
        return create(meta.getSlots(), meta.getDefaultItem());
    }

    static @NotNull Element fromIcon(List<Integer> slots, Icon icon) {
        return new Element() {
            @Override
            public @NotNull List<Integer> getSlots() {
                return slots;
            }

            @Override
            public ItemStack getItem() {
                return icon.getItem();
            }

            @Override
            public void onClick(@NotNull ClickContext context) {
                icon.onClick(context);
            }
        };
    }

    @NotNull List<Integer> getSlots();

    default void render(AbstractMenu menu) {
        ItemStack item = getItem();

        for (int slot : getSlots()) {
            menu.setClick(slot, this);
            menu.setItem(slot, item);
        }
    }

}
