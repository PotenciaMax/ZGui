package me.zmaster.zgui.inventory.element.view;

import me.zmaster.zgui.inventory.element.Clickable;

import java.util.List;

public interface IconsView<T extends Clickable> extends ElementsView<T> {

    List<Integer> getSlots();

    default void addSlots(List<Integer> slots) {
        getSlots().addAll(slots);
    }

    default void removeSlots(List<Integer> slots) {
        getSlots().removeAll(slots);
    }
}
