package me.zmaster.zgui.inventory.element;

import me.zmaster.zgui.inventory.context.ClickContext;

@FunctionalInterface
public interface Clickable {
    void onClick(ClickContext click);
}
