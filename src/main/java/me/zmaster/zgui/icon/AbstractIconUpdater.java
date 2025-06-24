package me.zmaster.zgui.icon;

import me.zmaster.zgui.menu.AbstractMenu;

import java.util.TreeSet;

public abstract class AbstractIconUpdater {

    private final TreeSet<Integer> slots = new TreeSet<>();

    public TreeSet<Integer> getSlots() {
        return slots;
    }

    public void addSlot(char... slot) {
        slots.addAll(getMenu().getSlotPattern().getSlotsByChar(slot));
    }

    public void removeSlot(char... slot) {
        slots.removeAll(getMenu().getSlotPattern().getSlotsByChar(slot));
    }

    public abstract AbstractMenu getMenu();

    public abstract void update();

}
