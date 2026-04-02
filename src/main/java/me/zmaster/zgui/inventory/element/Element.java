package me.zmaster.zgui.inventory.element;

import me.zmaster.zgui.inventory.AbstractMenu;
import me.zmaster.zgui.inventory.element.view.ElementsView;
import me.zmaster.zgui.inventory.meta.ElementMeta;
import me.zmaster.zgui.inventory.meta.MenuMeta;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Functional interface representing an icon in a menu.
 * Provides the item to be displayed and optionally handles click actions.
 */
@FunctionalInterface
public interface Element {

    static Element from(ItemStack item) {
        return () -> item;
    }

    static Element from(ItemStack item, Consumer<InventoryClickEvent> click) {
        return new Element() {
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

    static <M extends ElementMeta, E extends Element> E applyElement(
            MenuMeta<M> menuMeta, String key, Function<M, E> function, BiConsumer<List<Integer>, E> consumer) {

        M meta = menuMeta.getElementMeta(key);
        if (meta == null) return null;

        E element = function.apply(meta);
        if (element == null) return null;

        consumer.accept(meta.getSlots(), element);
        return element;
    }


    /**
     * Returns the ItemStack that represents this icon in the inventory.
     *
     * @return the item to be displayed
     */
    ItemStack getItem();

    /**
     * Called when the icon is clicked in the inventory.
     * Default implementation does nothing, so implementing classes
     * can override this method to define custom click behavior.
     *
     * @param event the InventoryClickEvent triggered by the click
     */
    default void onClick(InventoryClickEvent event) {}
}
