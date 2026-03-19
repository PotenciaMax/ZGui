package me.zmaster.zgui.inventory.element;

import me.zmaster.zgui.inventory.Slot;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * Functional interface representing an icon in a menu.
 * Provides the item to be displayed and optionally handles click actions.
 */
@FunctionalInterface
public interface Icon extends Element {

    static Icon from(ItemStack item) {
        return () -> item;
    }

    static Icon from(ItemStack item, Consumer<InventoryClickEvent> click) {
        return new Icon() {
            @Override
            public ItemStack getItem() {
                return item;
            }

            @Override
            public void onClick(InventoryClickEvent event) {
                click.accept(event);
            }
        };
    }

    /**
     * Returns the ItemStack that represents this icon in the inventory.
     *
     * @return the item to be displayed
     */
    ItemStack getItem();

    @Override
    default void render(Slot slot) {
        slot.setItem(getItem());
    }
}
