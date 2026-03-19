package me.zmaster.zgui.inventory.element.view;

import me.zmaster.zgui.inventory.AbstractMenu;
import me.zmaster.zgui.inventory.element.Element;
import me.zmaster.zgui.inventory.Slot;
import me.zmaster.zgui.Menu;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of AbstractIconUpdater that updates static icons in a menu.
 * It iterates over the assigned slots and refreshes the items in the inventory
 * based on the current icon data stored in the menu.
 */
public class SimpleElementView implements ElementView {

    private final List<Integer> slots = new ArrayList<>();
    private final AbstractMenu menu;

    SimpleElementView(@NotNull AbstractMenu menu) {
        this.menu = Objects.requireNonNull(menu, "menu cannot be null");
    }

    @Override
    public List<Integer> getSlots() {
        return slots;
    }

    @NotNull
    @Override
    public Menu getMenu() {
        return menu;
    }

    @Override
    public void update() {
        for (int index : getSlots()) {
            Slot slot = menu.getSlot(index);
            Element element = slot.getElement();
            if (element != null) element.render(slot);
        }
    }
}
