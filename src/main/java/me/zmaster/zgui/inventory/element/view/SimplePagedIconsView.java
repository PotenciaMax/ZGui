package me.zmaster.zgui.inventory.element.view;

import me.zmaster.zgui.inventory.AbstractMenu;
import me.zmaster.zgui.inventory.element.Icon;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SimplePagedIconsView<T extends Icon> implements PagedIconsView<T> {

    private final AbstractMenu menu;
    private List<T> elements = new ArrayList<>();
    private List<Integer> slots = new ArrayList<>();
    private int page = 1;

    public SimplePagedIconsView(@NotNull AbstractMenu menu) {
        this.menu = Objects.requireNonNull(menu, "menu must not be null");
    }

    @Override
    public void update() {
        int pageSize = getSlots().size();
        if (pageSize == 0) return;

        int lastIconPos = elements.size() - 1;
        int startPos = pageSize * (page - 1);
        if (lastIconPos < startPos) {
            return;
        }

        renderElements(startPos, Math.min(pageSize * page - 1, lastIconPos));
    }

    @Override
    public @NotNull List<T> getElements() {
        return elements;
    }

    @Override
    public List<Integer> getSlots() {
        return slots;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void setPage(int page) {
        if (page < 1) throw new IndexOutOfBoundsException("page cannot be < 1");
        this.page = page;
    }

    @Override
    public int getLastPage() {
        int totalElements = elements.size();
        int totalSlots = slots.size();

        if (totalSlots == 0) {
            // A paged menu must always have at least one page
            return 1;
        }

        return (int) Math.ceil((double) totalElements / totalSlots);
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }

    public void setSlots(List<Integer> slots) {
        this.slots = slots;
    }

    private void renderElements(int startPos, int lastPos) {
        int pos = startPos;

        for (int slot : slots) {
            if (pos > lastPos) {
                menu.setClick(slot, null);
                menu.setItem(slot, null);
                continue;
            }

            T element = elements.get(pos);
            menu.setClick(slot, element);
            menu.setItem(slot, element.getItem());

            pos++;
        }
    }

}
