package me.zmaster.zgui.inventory.element.view;

import me.zmaster.zgui.inventory.AbstractMenu;
import me.zmaster.zgui.inventory.element.Icon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class SortedPagedIconsView<I extends Icon> implements PagedIconsView<I> {

    private final SimplePagedIconsView<I> delegate;
    private List<I> allElements = new ArrayList<>();
    private Predicate<? super I> filter;
    private Comparator<? super I> comparator;

    public SortedPagedIconsView(AbstractMenu menu) {
        this.delegate = new SimplePagedIconsView<>(menu);
    }

    @Override
    public List<Integer> getSlots() {
        return delegate.getSlots();
    }

    @Override
    public void update() {
        delegate.setElements(new ArrayList<>(allElements));

        if (filter != null) delegate.getElements().removeIf(filter.negate());
        if (comparator != null) delegate.getElements().sort(comparator);

        delegate.update();
    }

    @Override
    public List<I> getElements() {
        return allElements;
    }

    @Override
    public int getPage() {
        return delegate.getPage();
    }

    @Override
    public void setPage(int page) {
        delegate.setPage(page);
    }

    @Override
    public int getLastPage() {
        return delegate.getLastPage();
    }

    public void setSlots(List<Integer> slots) {
        delegate.setSlots(slots);
    }

    public void setElements(List<I> elements) {
        this.allElements = elements;
    }

    public Predicate<? super I> getFilter() {
        return filter;
    }

    public void setFilter(Predicate<? super I> filter) {
        this.filter = filter;
    }

    public Comparator<? super I> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<? super I> comparator) {
        this.comparator = comparator;
    }
}
