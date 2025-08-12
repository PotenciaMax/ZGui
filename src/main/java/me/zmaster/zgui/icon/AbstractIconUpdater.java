package me.zmaster.zgui.icon;

import me.zmaster.zgui.AbstractMenu;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractIconUpdater {

    private final List<Integer> slots = new LinkedList<>();

    /**
     * Returns the list of slot indices that this updater will handle.
     *
     * @return the list of slot indices
     */
    public List<Integer> getSlots() {
        return slots;
    }

    /**
     * Adds the given slot indices to the list of slots handled by this updater.
     *
     * @param slots the slot indices to add
     */
    public void addSlot(List<Integer> slots) {
        this.slots.addAll(slots);
    }

    /**
     * Removes the given slot indices from the list of slots handled by this updater.
     *
     * @param slots the slot indices to remove
     */
    public void removeSlot(List<Integer> slots) {
        this.slots.removeAll(slots);
    }

    /**
     * Returns the menu associated with this updater.
     *
     * @return the menu instance
     */
    public abstract AbstractMenu getMenu();

    /**
     * Updates the icons in the specified slots of the menu.
     */
    public abstract void update();

}
