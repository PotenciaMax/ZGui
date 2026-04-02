package me.zmaster.zgui.inventory.element.view;

import me.zmaster.zgui.inventory.AbstractMenu;
import me.zmaster.zgui.inventory.element.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PagedElementsView<E extends Element> implements View {

    private final List<E> elements = new ArrayList<>();
    private final AbstractMenu menu;
    private List<Integer> slots = new ArrayList<>();
    private Comparator<? super E> comparator;
    private int page = 1;

    public PagedElementsView(@NotNull AbstractMenu menu) {
        this.menu = Objects.requireNonNull(menu, "menu must not be null");
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

        if (comparator != null) elements.sort(comparator);

        int lastIconPos = elements.size() - 1;
        int startPos = pageSize * (page - 1);
        if (lastIconPos < startPos) {
            return;
        }

        renderElements(startPos, Math.min(pageSize * page - 1, lastIconPos));
    }

    public List<Integer> getSlots() {
        return Collections.unmodifiableList(slots);
    }

    public void setSlots(List<Integer> slots) {
        this.slots = slots;
    }

    public void addSlots(List<Integer> slots) {
        this.slots.addAll(slots);
    }

    public void removeSlots(List<Integer> slots) {
        this.slots.removeAll(slots);
    }

    public @NotNull List<E> getElements() {
        return elements;
    }

    public void addElement(E element) {
        this.elements.add(element);
    }

    public @Nullable Comparator<? super E> getComparator() {
        return comparator;
    }

    public void setComparator(@Nullable Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Returns the current page number.
     */
    public int getPage() {
        return page;
    }

    public int getLastPage() {
        int totalElements = elements.size();
        int totalSlots = slots.size();

        if (totalSlots == 0) {
            // A paged menu must always have at least one page
            return 1;
        }

        return (int) Math.ceil((double) totalElements / totalSlots);
    }

    public void setPage(int page) {
        if (page < 1) throw new IndexOutOfBoundsException("page cannot be < 1");
        this.page = page;
    }

    private void renderElements(int startPos, int lastPos) {
        int pos = startPos;

        for (int i : slots) {
            if (pos > lastPos) {
                menu.setElement(i, null);
                continue;
            }

            menu.setElement(i, elements.get(pos));
            pos++;
        }
    }

}
