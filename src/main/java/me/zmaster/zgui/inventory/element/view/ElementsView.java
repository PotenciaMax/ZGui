package me.zmaster.zgui.inventory.element.view;


import me.zmaster.zgui.inventory.AbstractMenu;
import me.zmaster.zgui.inventory.element.Element;
import me.zmaster.zgui.inventory.meta.ElementMeta;
import me.zmaster.zgui.inventory.meta.MenuMeta;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class ElementsView implements View {

    private final Map<Element, List<Integer>> elements = new HashMap<>();
    private final AbstractMenu menu;

    public ElementsView(AbstractMenu menu) {
        this.menu = Objects.requireNonNull(menu, "menu must not be null");
    }

    @Override
    public void update() {
        for (Map.Entry<Element, List<Integer>> entry : elements.entrySet()) {
            menu.setElement(entry.getValue(), entry.getKey());
        }
    }

    public Map<Element, List<Integer>> getElements() {
        return elements;
    }

    public void setElement(List<Integer> slots, Element element) {
        elements.put(element, slots);
    }

    public @Nullable <M extends ElementMeta, T extends Element> T applyElement(MenuMeta<M> menuMeta, String key, Function<M, T> factory) {
        return Element.applyElement(menuMeta, key, factory, this::setElement);
    }

}
