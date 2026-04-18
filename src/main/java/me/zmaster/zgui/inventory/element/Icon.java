package me.zmaster.zgui.inventory.element;

import me.zmaster.zgui.inventory.context.ClickContext;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface Icon extends Clickable {

    ItemStack getItem();

    @Override
    default void onClick(ClickContext context) {}
}
