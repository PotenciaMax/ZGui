package me.zmaster.zgui.element.view;

import me.zmaster.zgui.Slot;
import me.zmaster.zgui.PagedMenu;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Icon updater specialized for paged menus.
 * It manages the display of icons across multiple pages by updating
 * only the icons visible on the current page.
 */
public class PagedElementsView implements ElementView {

    private final List<Integer> slots = new ArrayList<>();
    private final PagedMenu<?> menu;
    private int page = 1;

    public PagedElementsView(@NotNull PagedMenu<?> menu) {
        this.menu = Objects.requireNonNull(menu, "menu cannot be null");
    }

    @Override
    public List<Integer> getSlots() {
        return slots;
    }

    @Override
    public PagedMenu<?> getMenu() {
        return menu;
    }

    /**
     * Updates the icons on the current page in the menu.
     */
    @Override
    public void update() {
        int pageSize = getSlots().size();
        if (pageSize == 0) {
            return;
        }

        menu.sortIcons();

        int lastIconPos = menu.getPagedElements().size() - 1;
        int startPos = pageSize * (page - 1);
        if (lastIconPos < startPos) {
            return;
        }

        renderElements(startPos, Math.min(pageSize * page - 1, lastIconPos));
    }

    private void renderElements(int startPos, int lastPos) {
        int pos = startPos;

        for (int i : slots) {
            Slot slot = menu.getSlot(i);

            if (pos > lastPos) {
                slot.removeElement();
                continue;
            }

            slot.setElement(menu.getPagedElements().get(pos));
            pos++;
        }
    }

    /**
     * Returns the current page number.
     */
    public int getPage() {
        return page;
    }

    /**
     * Calculates and returns the last page number based on the total icons and slots available.
     */
    public int getLastPage() {
        int totalElements = menu.getPagedElements().size();
        int totalSlots = getSlots().size();

        if (totalSlots == 0) {
            // A paged menu must always have at least one page
            return 1;
        }

        return (int) Math.ceil((double) totalElements / totalSlots);
    }

    /**
     * Sets the current page number.
     * @param page the new page number, must be >= 1
     * @throws IndexOutOfBoundsException if page < 1
     */
    public void setPage(int page) {
        if (page < 1) throw new IndexOutOfBoundsException("page cannot be < 1");
        this.page = page;
    }

}
