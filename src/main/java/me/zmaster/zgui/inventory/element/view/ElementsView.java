package me.zmaster.zgui.inventory.element.view;

import me.zmaster.zgui.inventory.element.Clickable;

import java.util.Collection;

public interface ElementsView<T extends Clickable> {

    void update();

    Collection<T> getElements();

    default void addElement(T element) {
        getElements().add(element);
    }

    default void removeElement(T element) {
        getElements().remove(element);
    }
}
