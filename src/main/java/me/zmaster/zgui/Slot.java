package me.zmaster.zgui;

import me.zmaster.zgui.element.Element;
import org.bukkit.inventory.ItemStack;

public class Slot {

    private final AbstractMenu menu;
    private final int index;

    Slot(AbstractMenu menu, int index) {
        this.menu = menu;
        this.index = index;
    }

    public Menu getMenu() {
        return menu;
    }

    public int getIndex() {
        return index;
    }

    public Element getElement() {
        return menu.elements.get(index);
    }

    public void setElement(Element element) {
        setElement(element, true);
    }

    public void setElement(Element element, boolean render) {
        menu.elements.put(index, element);
        if (render) element.render(this);
    }

    public ItemStack getItem() {
        return menu.inventory.getItem(index);
    }

    public void setItem(ItemStack item) {
        menu.inventory.setItem(index, item);
    }

    public void removeElement() {
        menu.elements.remove(index);
        setItem(null);
    }
}
