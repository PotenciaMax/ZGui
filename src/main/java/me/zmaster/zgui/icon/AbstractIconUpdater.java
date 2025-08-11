package me.zmaster.zgui.icon;

import me.zmaster.zgui.menu.AbstractMenu;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractIconUpdater {

    private final List<Integer> slots = new LinkedList<>();

    public List<Integer> getSlots() {
        return slots;
    }

    public void addSlot(List<Integer> slots) {
        this.slots.addAll(slots);
    }

    public void removeSlot(List<Integer> slots) {
        this.slots.addAll(slots);
    }

    public abstract AbstractMenu getMenu();

    public abstract void update();

}
