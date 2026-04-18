package me.zmaster.zgui.inventory.element.view;

import me.zmaster.zgui.inventory.AbstractMenu;
import me.zmaster.zgui.inventory.element.Element;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SimpleElementsView<E extends Element> implements ElementsView<E> {

    private final AbstractMenu menu;
    private final Collection<E> elements = new HashSet<>();

    public SimpleElementsView(AbstractMenu menu) {
        this.menu = menu;
    }

    @Override
    public Collection<E> getElements() {
        return elements;
    }

    @Override
    public void update() {
        for (Element element : elements) {
            ItemStack item = element.getItem();

            for (int slot : element.getSlots()) {
                menu.setClick(slot, element);
                menu.setItem(slot, item);
            }
        }
    }

}
