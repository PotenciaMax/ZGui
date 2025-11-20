package me.zmaster.zgui.element.view;

import me.zmaster.zgui.Menu;
import java.util.List;
import java.util.Objects;

/**
 * Abstract base class responsible for managing and updating icons within a menu.
 * It maintains a list of inventory slots that should be updated and defines
 * the contract for subclasses to specify the menu and update logic.
 * <p>
 * Subclasses must implement how the icons are applied to the menu inventory.
 */
public interface ElementView {

    static ElementView create(Menu menu) {
        return new SimpleElementView(menu);
    }

    static ElementView create(Menu menu, List<Integer> slots) {
        ElementView view = create(menu);
        view.addSlots(Objects.requireNonNull(slots, "slots cannot be null"));
        return view;
    }

    static ElementView create(Menu menu, int slot) {
        ElementView view = create(menu);
        view.addSlot(slot);
        return view;
    }

    /**
     * Returns the list of slot indices that this updater will handle.
     *
     * @return the list of slot indices
     */
    List<Integer> getSlots();

    /**
     * Returns the menu associated with this updater.
     *
     * @return the menu instance
     */
    Menu getMenu();

    /**
     * Updates the icons in the specified slots of the menu.
     */
    void update();

    default void addSlot(int slot) {
        getSlots().add(slot);
    }

    default void removeSlot(int slot) {
        getSlots().remove(slot);
    }

    /**
     * Adds the given slot indices to the list of slots handled by this updater.
     *
     * @param slots the slot indices to add
     */
    default void addSlots(List<Integer> slots) {
        getSlots().addAll(slots);
    }

    /**
     * Removes the given slot indices from the list of slots handled by this updater.
     *
     * @param slots the slot indices to remove
     */
    default void removeSlots(List<Integer> slots) {
        getSlots().removeAll(slots);
    }

}
