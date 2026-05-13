package me.zmaster.zgui.inventory.element.view;

import me.zmaster.zgui.inventory.AbstractMenu;
import me.zmaster.zgui.inventory.element.Element;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SimpleElementsView<E extends Element> implements ElementsView<E> {

    private final AbstractMenu menu;
    private final Collection<E> elements = new HashSet<>();

    public SimpleElementsView(AbstractMenu menu) {
        this.menu = menu;
    }

    @Override
    public @NotNull Collection<E> getElements() {
        return elements;
    }

    @Override
    public void update() {
        elements.forEach(element -> element.render(menu));
    }

}
